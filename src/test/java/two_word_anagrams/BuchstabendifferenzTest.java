package two_word_anagrams;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuchstabendifferenzTest {

    private static final Wort DOCUMENTING = new Wort("documenting");

    @Test
    void wortKuerzerAlsZielwortIstGeeignet() {
        assertTrue(Buchstabendifferenz.istGeeignet(new Wort("document"), DOCUMENTING));
    }

    @Test
    void wortGleichLangWieZielwortIstNichtGeeignet() {
        assertFalse(Buchstabendifferenz.istGeeignet(DOCUMENTING, DOCUMENTING));
    }

    @Test
    void wortMitFremdemBuchstabeIstNichtGeeignet() {
        assertFalse(Buchstabendifferenz.istGeeignet(new Wort("xyz"), DOCUMENTING));
    }

    @Test
    void wortMitZuHaeufigEmBuchstabeIstNichtGeeignet() {
        // "n" kommt in "documenting" zweimal vor, dreimal ist zu oft
        assertFalse(Buchstabendifferenz.istGeeignet(new Wort("nnn"), DOCUMENTING));
    }

    @Test
    void differenzVonDocumentGegenZielwortGleichDifferenzVonGin() {
        // "documenting" - "document" = g, i, n  →  entspricht den Buchstaben von "gin"
        Buchstabendifferenz differenzVonDocument = Buchstabendifferenz.vonWortGegenZielwort(new Wort("document"), DOCUMENTING);
        Buchstabendifferenz differenzVonGin = Buchstabendifferenz.vonWort(new Wort("gin"));

        assertEquals(differenzVonDocument, differenzVonGin);
    }

    @Test
    void differenzVonGinGegenZielwortGleichDifferenzVonDocument() {
        // "documenting" - "gin" = c, d, e, m, n, o, t, u  →  entspricht den Buchstaben von "document"
        Buchstabendifferenz differenzVonGin = Buchstabendifferenz.vonWortGegenZielwort(new Wort("gin"), DOCUMENTING);
        Buchstabendifferenz differenzVonDocument = Buchstabendifferenz.vonWort(new Wort("document"));

        assertEquals(differenzVonGin, differenzVonDocument);
    }
}
