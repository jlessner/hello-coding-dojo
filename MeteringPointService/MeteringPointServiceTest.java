/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.fastlane.myBusinessSupplier.contract.pointOfDelivery;

import static com.nextlevel.fastlane.myBusinessSupplier.ContractStateEnum.GPKE_APPROVED;
import static com.nextlevel.platform.edi.enums.MeterPointType.Z30;
import static com.nextlevel.platform.edi.enums.MeterPointType.Z31;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.nextlevel.fastlane.BaseTest;
import com.nextlevel.fastlane.financial.bo.TimeSlice;
import com.nextlevel.fastlane.myBusinessSupplier.ContractCommunicationTypeEnum;
import com.nextlevel.fastlane.myBusinessSupplier.ContractStateEnum;
import com.nextlevel.fastlane.myBusinessSupplier.PersistenceHandler;
import com.nextlevel.fastlane.myBusinessSupplier.bo.Contract;
import com.nextlevel.fastlane.myBusinessSupplier.bo.ContractAdditional;
import com.nextlevel.fastlane.myBusinessSupplier.bo.ControllableResource;
import com.nextlevel.fastlane.myBusinessSupplier.bo.PointOfDelivery;
import com.nextlevel.fastlane.myBusinessSupplier.bo.TechnicalResource;
import com.nextlevel.myBusinessSupplier.GlobalPropertiesEnum;
import com.nextlevel.myBusinessSupplier.aepmako.enums.domain.entity.nachrichten.Meldepunkttyp;
import com.nextlevel.platform.date.DateTool;

public class MeteringPointServiceTest extends BaseTest {
	private final String maloQualifier = Meldepunkttyp.MARKTLOKATION.name();
	private final String meloQualifier = Meldepunkttyp.MESSLOKATION.name();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void test() throws Exception {
		PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

		Contract contract = new Contract();
		contract.setCommunicationType(ContractCommunicationTypeEnum.EMAIL.name());
		persistenceHandler.save(contract);

		PointOfDelivery marketLocation = MeteringPointService.getMarketLocation(contract);
		assertNull(marketLocation);

		PointOfDelivery market = new PointOfDelivery();
		persistenceHandler.save(market);
		persistenceHandler.save(contract.withPointOfDelivery(market.getId()));

		marketLocation = MeteringPointService.getMarketLocation(contract);
		assertEquals(market.getId(), marketLocation.getId());

		PointOfDelivery fallback = MeteringPointService.getMeteringLocation(contract);
		assertEquals(market.getId(), fallback.getId());

		PointOfDelivery metering = new PointOfDelivery()
				.withParent(market.getId())
				.withMeteringPointType(this.meloQualifier);
		persistenceHandler.save(metering);

		PointOfDelivery meteringLocation = MeteringPointService.getMeteringLocation(contract);
		assertEquals(metering.getId(), meteringLocation.getId());
	}

	@Test
	public void testGetPointOfDelivery() throws Exception {
		PointOfDelivery malo = createPointOfDelivery("Malo1234", maloQualifier, null);

		Date periodStart = DateTool.getDate(1, 1, 2018);
		PointOfDelivery pointOfDelivery = MeteringPointService.getPointOfDelivery("Malo1234", Z30, DateTool.getDate(1, 1, 2018));
		assertEquals(malo.getId(), pointOfDelivery.getId());

		// contract in slice
		Contract firstContract = createContract(malo, periodStart, GPKE_APPROVED);

		// contract outside slice with same malo data
		PointOfDelivery secondMalo = createPointOfDelivery("Malo1234", maloQualifier, null);
		createContract(secondMalo, DateTool.getStartOfNextDay(periodStart), GPKE_APPROVED);

		{ // get by malo
			pointOfDelivery = MeteringPointService.getPointOfDelivery("Malo1234", Z30, periodStart);
			assertEquals(malo.getId(), pointOfDelivery.getId());
		}

		{ // get by melo info
			PointOfDelivery melo = createPointOfDelivery("Melo", meloQualifier, malo.getId());

			// same data in different slice
			createPointOfDelivery("Melo", meloQualifier, secondMalo.getId());

			pointOfDelivery = MeteringPointService.getPointOfDelivery("Melo", Z31, periodStart);
			assertEquals(melo.getId(), pointOfDelivery.getId());
		}

		{ // both in slice but only one is approved
			firstContract.withState(ContractStateEnum.GPKE_RUNNING.name()).save();

			pointOfDelivery = MeteringPointService.getPointOfDelivery("Malo1234", Z30, DateTool.getCurrent());
			assertEquals(secondMalo.getId(), pointOfDelivery.getId());
		}
	}

	@Test
	public void getGetPointOfDeliveryException() throws Exception {
		createContract(createPointOfDelivery("malo", maloQualifier, null), DateTool.getStartOfToday(), GPKE_APPROVED);
		createContract(createPointOfDelivery("malo", maloQualifier, null), DateTool.getStartOfTomorrow(), GPKE_APPROVED);

		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("More than one point of delivery");
		expectedException.expectMessage("malo");
		MeteringPointService.getPointOfDelivery("malo", Z30, DateTool.getStartOfTomorrow());
	}

	@Test
	public void testGetContract() throws Exception {
		Contract firstContract =
				createContract(createPointOfDelivery("malo", maloQualifier, null), DateTool.getStartOfToday(), GPKE_APPROVED);
		createContract(createPointOfDelivery("malo", maloQualifier, null), DateTool.getStartOfTomorrow(), GPKE_APPROVED);

		Contract contract = MeteringPointService.getContractByMarketLocation("malo", DateTool.getStartOfYesterday());
		assertNull(contract);

		setGlobalProperty(GlobalPropertiesEnum.CONTRACT_SUPPLY_BEGIN_DATE_VALUESOURCE, "ADDITIONAL3");

		ContractAdditional contractAdditional = new ContractAdditional().withDateAdditional3(DateTool.getStartOfYesterday());
		contractAdditional.save();
		firstContract.withContractAdditionalId(contractAdditional.getId()).save();

		contract = MeteringPointService.getContractByMarketLocation("malo", DateTool.getStartOfYesterday());
		assertEquals(firstContract.getId(), contract.getId());
	}

	@Test
	public void testListContractsByMarketLocation() throws Exception {

		Contract first = createContract(createPointOfDelivery("malo", maloQualifier, null), DateTool.getStartOfToday(), GPKE_APPROVED);
		Contract second = createContract(createPointOfDelivery("malo", maloQualifier, null), DateTool.getStartOfTomorrow(), GPKE_APPROVED);

		createContract(createPointOfDelivery("malo2", maloQualifier, null), DateTool.getStartOfToday(), GPKE_APPROVED);
		createContract(createPointOfDelivery("malo2", maloQualifier, null), DateTool.getStartOfTomorrow(), GPKE_APPROVED);

		List<Contract> contracts = MeteringPointService.listContractsByMarketLocation("malo");
		assertEquals(2, contracts.size());
		assertTrue(contracts.stream().anyMatch(contract -> contract.getId().equals(first.getId())));
		assertTrue(contracts.stream().anyMatch(contract -> contract.getId().equals(second.getId())));
	}

	@Test
	public void testListContractsByMeteringLocation() throws Exception {
		Contract first =
				createContract(createPointOfDelivery("malo", maloQualifier, createPointOfDelivery("melo", meloQualifier, null).getId()),
						DateTool.getStartOfToday(), GPKE_APPROVED);
		Contract second =
				createContract(createPointOfDelivery("malo", maloQualifier, createPointOfDelivery("melo", meloQualifier, null).getId()),
						DateTool.getStartOfTomorrow(), GPKE_APPROVED);

		createContract(createPointOfDelivery("malo2", maloQualifier, createPointOfDelivery("melo2", meloQualifier, null).getId()),
				DateTool.getStartOfToday(),
				GPKE_APPROVED);
		createContract(createPointOfDelivery("malo2", maloQualifier, createPointOfDelivery("melo3", meloQualifier, null).getId()),
				DateTool.getStartOfTomorrow(), GPKE_APPROVED);

		List<Contract> contracts = MeteringPointService.listContractsByMarketLocation("malo");
		assertEquals(2, contracts.size());
		assertTrue(contracts.stream().anyMatch(contract -> contract.getId().equals(first.getId())));
		assertTrue(contracts.stream().anyMatch(contract -> contract.getId().equals(second.getId())));
	}

	@Test
	public void testGetContractByPointOfDelivery() throws Exception {
		PointOfDelivery malo = createPointOfDelivery("malo", maloQualifier, null);
		PointOfDelivery melo = createPointOfDelivery("melo", meloQualifier, malo.getId());
		Contract contract = createContract(malo, DateTool.getStartOfToday(), GPKE_APPROVED);

		PointOfDelivery malo2 = createPointOfDelivery("malo2", maloQualifier, null);
		PointOfDelivery melo2 = createPointOfDelivery("melo2", meloQualifier, malo2.getId());
		Contract contract2 = createContract(malo2, DateTool.getStartOfToday(), GPKE_APPROVED);

		// byMeteringLocation
		Contract result = BaseContractFinder.getContractByMeteringLocation(melo);
		assertEquals(contract.getId(), result.getId());

		result = BaseContractFinder.getContractByMeteringLocation(melo2);
		assertEquals(contract2.getId(), result.getId());

		// byMarketLocation
		result = BaseContractFinder.getContractByMarketLocation(malo);
		assertEquals(contract.getId(), result.getId());

		result = BaseContractFinder.getContractByMarketLocation(malo2);
		assertEquals(contract2.getId(), result.getId());
	}

	@Test
	public void testGetContractByMarketLocationAndTimeSlice() throws Exception {
		PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

		// create a contract thats terminated after the tested time slice
		PointOfDelivery malo1 = createPointOfDelivery("malo2", maloQualifier, null);
		Contract contract1 = createContract(malo1, DateTool.getStartOfToday(), GPKE_APPROVED);
		contract1.setPeriodStart(DateTool.create(2019, 3, 1));
		persistenceHandler.save(contract1);

		// create a contract thats terminated within the tested time slice
		PointOfDelivery malo2 = createPointOfDelivery("malo2", maloQualifier, null);
		Contract contract2 = createContract(malo2, DateTool.getStartOfToday(), GPKE_APPROVED);
		contract2.setPeriodStart(DateTool.create(2019, 1, 1));
		persistenceHandler.save(contract2);

		// create a contract thats terminated before the tested time slice
		PointOfDelivery malo3 = createPointOfDelivery("malo3", maloQualifier, null);
		Contract contract3 = createContract(malo3, DateTool.getStartOfToday(), GPKE_APPROVED);
		contract3.setPeriodStart(DateTool.create(2018, 11, 1));
		contract3.setPeriodEnd(DateTool.create(2019, 0, 31));
		persistenceHandler.save(contract3);

		// execute the logic
		final TimeSlice timeSlice = new TimeSlice()
				.withPeriodStart(DateTool.create(2019, 1, 1))
				.withPeriodEnd(DateTool.create(2019, 2, 30));
		Contract result = MeteringPointService.getContractByMarketLocation("malo2", timeSlice);

		assertEquals(result.getId(), contract2.getId());

		// create a second contract thats terminated within the tested time slice
		PointOfDelivery malo4 = createPointOfDelivery("malo2", maloQualifier, null);
		Contract contract4 = createContract(malo4, DateTool.getStartOfToday(), GPKE_APPROVED);
		contract4.setPeriodStart(DateTool.create(2019, 1, 5));
		contract4.setPeriodEnd(DateTool.create(2019, 2, 15));
		persistenceHandler.save(contract4);

		// now 2 contract will be found and a exception shall be thrown
		try {
			MeteringPointService.getContractByMarketLocation("malo2", timeSlice);
			fail("An exception should be thrown, if more than one contract will be found!");
		} catch (IllegalStateException e) {
			assertTrue(e.getMessage().contains("More than one point of delivery for meteringPoint malo2 within time slice"));
		}
	}

	@Test
	public void testGetContractByMarketLocationForOneDaySlice() throws Exception {
		PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

		PointOfDelivery malo1 = createPointOfDelivery("malo2", maloQualifier, null);
		Contract contract1 = createContract(malo1, DateTool.getStartOfToday(), GPKE_APPROVED);
		contract1.setPeriodEnd(DateTool.getEndOfYesterday());
		persistenceHandler.save(contract1);

		PointOfDelivery malo2 = createPointOfDelivery("malo2", maloQualifier, null);
		Contract contract2 = createContract(malo2, DateTool.getStartOfToday(), GPKE_APPROVED);
		persistenceHandler.save(contract2);

		final TimeSlice timeSlice = new TimeSlice()
				.withPeriodStart(DateTool.getStartOfToday())
				.withPeriodEnd(DateTool.getStartOfToday());
		Contract result = MeteringPointService.getContractByMarketLocation("malo2", timeSlice);

		assertEquals(contract2.getId(), result.getId());
	}

	@Test
	public void testGetContractByMeteringLocationForOneDaySlice() throws Exception {
		PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

		PointOfDelivery malo1 = createPointOfDelivery("malo1", maloQualifier, null);
		PointOfDelivery melo1 = createPointOfDelivery("melo1", meloQualifier, malo1.getId());
		Contract contract1 = createContract(malo1, DateTool.getStartOfToday(), GPKE_APPROVED);
		persistenceHandler.save(contract1);

		PointOfDelivery malo2 = createPointOfDelivery("malo2", maloQualifier, null);
		PointOfDelivery melo2 = createPointOfDelivery("melo2", meloQualifier, malo2.getId());
		Contract contract2 = createContract(malo2, DateTool.getStartOfToday(), GPKE_APPROVED);
		persistenceHandler.save(contract2);

		final TimeSlice timeSlice = new TimeSlice()
				.withPeriodStart(DateTool.getStartOfToday())
				.withPeriodEnd(DateTool.getEndOfToday());
		Contract result1 = MeteringPointService.getContractByMeteringLocation("melo1", timeSlice);

		assertEquals(contract1.getId(), result1.getId());

		Contract result2 = MeteringPointService.getContractByMeteringLocation("melo2", timeSlice);
		assertEquals(contract2.getId(), result2.getId());
	}

	@Test
	public void testGetContractByMeteringLocationFail() throws Exception {
		PointOfDelivery malo = createPointOfDelivery("malo", maloQualifier, null);
		PointOfDelivery melo = createPointOfDelivery("melo", meloQualifier, malo.getId());
		createContract(malo, DateTool.getStartOfToday(), GPKE_APPROVED);
		createContract(malo, DateTool.getStartOfToday(), GPKE_APPROVED);

		expectedException.expectMessage("Number of contracts found for PointOfDelivery with ID: " + melo.getId() + " was not one but 2");
		BaseContractFinder.getContractByMeteringLocation(melo);
	}

	@Test
	public void testNullPointerFixGetSinglePointOfDelivery() {
		try {
			MeteringPointService.getSinglePointOfDelivery(null, Z30);
			fail("Exception should have been thrown");
		} catch (Exception e) {
			assertThat(e, notNullValue());
			assertTrue("expected exception is not of type " + NullPointerException.class.getSimpleName() + " but " + e.getClass()
					.getSimpleName(), e instanceof NullPointerException);
			assertThat(e.getMessage(), equalTo("Parameter 'contract' must not be NULL"));
		}
	}

	@Test
	public void testGetMarketLocationByMeteringLocation() throws Exception {
		final PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

		PointOfDelivery malo1 = createPointOfDelivery("malo1", maloQualifier, null);
		PointOfDelivery melo1 = createPointOfDelivery("melo1", meloQualifier, malo1.getId());
		Contract contract1 = createContract(malo1, DateTool.getStartOfToday(), GPKE_APPROVED);
		persistenceHandler.save(contract1);

		PointOfDelivery malo2 = createPointOfDelivery("malo2", maloQualifier, null);
		PointOfDelivery melo2 = createPointOfDelivery("melo2", meloQualifier, malo2.getId());
		Contract contract2 = createContract(malo1, DateTool.getStartOfToday(), GPKE_APPROVED);
		persistenceHandler.save(contract2);

		PointOfDelivery testResult = MeteringPointService.getMarketLocationByMeteringLocation(melo1);
		assertThat(testResult.getId(), comparesEqualTo(malo1.getId()));

		testResult = MeteringPointService.getMarketLocationByMeteringLocation(melo2);
		assertThat(testResult.getId(), comparesEqualTo(malo2.getId()));
	}

	@Test
	public void testGetContractByMeteringPoint() throws Exception {
		final PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

		PointOfDelivery malo = createPointOfDelivery("malo", maloQualifier, null);
		PointOfDelivery melo = createPointOfDelivery("melo", meloQualifier, malo.getId());
		Contract contract = createContract(malo, DateTool.getStartOfToday(), GPKE_APPROVED);
		persistenceHandler.save(contract);

		Contract testResult = MeteringPointService.getContractByMeteringPoint(malo);
		assertThat(testResult.getId(), is(contract.getId()));

		testResult = MeteringPointService.getContractByMeteringPoint(melo);
		assertThat(testResult.getId(), is(contract.getId()));
	}

	@Test
	public void testListContractByMeteringPoint() throws Exception {

		PointOfDelivery malo = createPointOfDelivery("malo", maloQualifier, null);
		PointOfDelivery melo = createPointOfDelivery("melo", meloQualifier, malo.getId());
		PointOfDelivery nelo = createPointOfDelivery("nelo", Meldepunkttyp.NETZLOKATION.name(), malo.getId());
		Contract contract = createContract(malo, DateTool.getStartOfToday(), GPKE_APPROVED);
		contract.save();

		List<Contract> results = MeteringPointService.listContractsByMeteringPoint("malo");
		assertEquals(1, results.size());
		assertEquals(contract.getId(), results.get(0).getId());

		results = MeteringPointService.listContractsByMeteringPoint("melo");
		assertEquals(1, results.size());
		assertEquals(contract.getId(), results.get(0).getId());

		results = MeteringPointService.listContractsByMeteringPoint("nelo");
		assertEquals(1, results.size());
	}

	@Test
	public void testGetContractByControllableResourceNotFound() throws Exception {
		Contract notFound = MeteringPointService.getContractByControllableResource("dummy", DateTool.getCurrent());
		assertThat(notFound, nullValue());
	}

	@Test
	public void testGetContractByControllableResourceFound() throws Exception {
		PointOfDelivery malo = new PointOfDelivery().withMeteringPoint("Malo123456");
		malo.save();
		Contract contract = new Contract().withContractNr("1").withPointOfDelivery(malo.getId());
		contract.save();
		ControllableResource controllableResource = new ControllableResource().withIdentification("C1111111111");
		controllableResource.save();
		TechnicalResource technicalResource = new TechnicalResource()
				.withMaloId(malo.getId())
				.withControllableResourceId(controllableResource.getId())
				.withIdentification("D1111111111");
		technicalResource.save();

		Contract found = MeteringPointService.getContractByControllableResource("C1111111111", DateTool.getCurrent());

		assertThat(found, notNullValue());
		assertThat(found.getId(), is(contract.getId()));
	}

	@Test
	public void testGetContractByTechnicalResourceNotFound() throws Exception {
		Contract notFound = MeteringPointService.getContractByTechnicalResource("dummy", DateTool.getCurrent());
		assertThat(notFound, nullValue());
	}

	@Test
	public void testGetContractByTechnicalResourceFound() throws Exception {
		PointOfDelivery malo = new PointOfDelivery().withMeteringPoint("Malo123456");
		malo.save();
		Contract contract = new Contract().withContractNr("1").withPointOfDelivery(malo.getId());
		contract.save();
		TechnicalResource technicalResource = new TechnicalResource().withMaloId(malo.getId()).withIdentification("D1111111111");
		technicalResource.save();

		Contract found = MeteringPointService.getContractByTechnicalResource("D1111111111", DateTool.getCurrent());

		assertThat(found, notNullValue());
		assertThat(found.getId(), is(contract.getId()));
	}

	private PointOfDelivery createPointOfDelivery(String meteringPoint, String meterPointType, Long parent) {
		PointOfDelivery malo = new PointOfDelivery()
				.withMeteringPoint(meteringPoint)
				.withMeteringPointType(meterPointType)
				.withParent(parent);
		malo.save();
		return malo;
	}

	private Contract createContract(PointOfDelivery malo, Date periodStart, ContractStateEnum state) {
		Contract contract = new Contract()
				.withPointOfDelivery(malo.getId())
				.withState(state.name());
		contract.setPeriodStart(periodStart);
		contract.save();
		return contract;
	}
}