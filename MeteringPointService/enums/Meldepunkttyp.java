/*
 * Copyright (C) Next Level Integration GmbH Germany - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.nextlevel.myBusinessSupplier.aepmako.enums.domain.entity.nachrichten;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public enum Meldepunkttyp {
	MARKTLOKATION("\\d{11}"),
	MESSLOKATION("^[A-Z0-9]{2}\\d{11}[A-Z0-9_]{20}$"),
	NETZLOKATION("[E][A-Za-z0-9]{10}"),
	TECHNISCHE_RESSOURCE("[D][A-Za-z0-9]{10}"),
	STEUERBARE_RESSOURCE("[C][A-Za-z0-9]{10}");

	private final String regEx;

	Meldepunkttyp(String regEx) {
		this.regEx = regEx;
	}

	public boolean isMeldepunkttyp(String meldepunkt) {
		if (StringUtils.isBlank(meldepunkt)) {
			return false;
		}
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(meldepunkt);
		return matcher.matches();
	}

	public static Meldepunkttyp getMeldepunkttyp(String meldepunkt) {
		for (Meldepunkttyp value : values()) {
			if (value.isMeldepunkttyp(meldepunkt)) {
				return value;
			}
		}
		return null;
	}
}
