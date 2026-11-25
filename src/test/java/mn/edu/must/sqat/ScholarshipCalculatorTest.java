package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * ScholarshipCalculator-ийн тестүүд.
 * Хэсэг A: зөвхөн хэвийн урсгал (happy path).
 */
class ScholarshipCalculatorTest {

    private final ScholarshipCalculator calc = new ScholarshipCalculator();

    // ---------- Хэсэг A: happy path ----------

    @Test
    void letterGrade_95_isA() {
        assertEquals("A", calc.letterGrade(95));
    }

    @Test
    void letterGrade_85_isB() {
        assertEquals("B", calc.letterGrade(85));
    }

    @Test
    void gpa_95and85_is3point5() {
        assertEquals(3.5, calc.gpa(new double[] {95, 85}), 1e-9);
    }

    @Test
    void scholarship_highGpaManyCredits_is100() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
    }

    @Test
    void scholarship_goodGpaEnoughCredits_is70() {
        assertEquals(70, calc.scholarship(3.5, 13, false));
    }

    // ---------- Хэсэг B: branch 100% ----------

    @Test
    void letterGrade_75_isC() {
        assertEquals("C", calc.letterGrade(75));
    }

    @Test
    void letterGrade_65_isD() {
        assertEquals("D", calc.letterGrade(65));
    }

    @Test
    void letterGrade_30_isF() {
        assertEquals("F", calc.letterGrade(30));
    }

    @Test
    void letterGrade_negative_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1));
    }

    @Test
    void letterGrade_over100_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101));
    }

    @Test
    void gpa_cdf_mix_is1() {
        // C=2, D=1, F=0 (switch-ийн default салаа) -> 3/3 = 1.0
        assertEquals(1.0, calc.gpa(new double[] {75, 65, 30}), 1e-9);
    }

    @Test
    void gpa_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
    }

    @Test
    void gpa_empty_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[] {}));
    }

    @Test
    void scholarship_negativeGpa_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(-0.1, 15, false));
    }

    @Test
    void scholarship_gpaOver4_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.1, 15, false));
    }

    @Test
    void scholarship_negativeCredits_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(3.5, -1, false));
    }

    @Test
    void scholarship_violation_is0() {
        assertEquals(0, calc.scholarship(3.9, 16, true));
    }

    @Test
    void scholarship_highGpaFewCredits_is70() {
        // gpa >= 3.8 Т, credits >= 15 Х -> дараагийн нөхцөл: 3.4/12 -> 70
        assertEquals(70, calc.scholarship(3.9, 13, false));
    }

    @Test
    void scholarship_goodGpaTooFewCredits_is50() {
        // gpa >= 3.4 Т, credits >= 12 Х -> gpa >= 3.0 -> 50
        assertEquals(50, calc.scholarship(3.5, 10, false));
    }

    @Test
    void scholarship_gpa3point2_is50() {
        assertEquals(50, calc.scholarship(3.2, 20, false));
    }

    @Test
    void scholarship_lowGpa_is0() {
        assertEquals(0, calc.scholarship(2.0, 20, false));
    }

    // ---------- Хэсэг C: давталтын зааг (0 / 1 / 2+ удаа) ----------

    @Test
    void gpa_loopZeroTimes_emptyAndNullThrow() {
        // 0 удаа: давталтад орохоос өмнө хамгаалалт ажиллах ёстой
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[] {}));
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
    }

    @Test
    void gpa_loopOnce_singleScore95_is4() {
        assertEquals(4.0, calc.gpa(new double[] {95}), 1e-9);
    }

    @Test
    void gpa_loopManyTimes_fourScores_is2point5() {
        // A=4, B=3, C=2, D=1 -> 10/4 = 2.5
        assertEquals(2.5, calc.gpa(new double[] {95, 85, 75, 65}), 1e-9);
    }
}
