package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportingExtension.class)
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	@Test
	public void sampleTest() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(7, 2, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/*
		line 14 - CUTPNFP tests
		Unique true points: "TFF", "FTF", "FFT"
		Near false points: "FFF
	 */

	@UniqueTruePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "a",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		})
	@Test
	void testEqualEdgesInAreCongruentWithFirstAsUniqueTruePoint() {
		Triangle triangle1 = new Triangle(2, 3, 5);
		Triangle triangle2 = new Triangle(3, 3, 5);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);

	}

	@UniqueTruePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false)
		})
	@Test
	void testEqualEdgesInAreCongruentWithSecondAsUniqueTruePoint() {
		Triangle triangle1 = new Triangle(2, 3, 5);
		Triangle triangle2 = new Triangle(2, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "c",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true)
		})
	@Test
	void testEqualEdgesInAreCongruentWithThirdAsUniqueTruePoint() {
		Triangle triangle1 = new Triangle(2, 3, 4);
		Triangle triangle2 = new Triangle(2, 3, 5);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@NearFalsePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "b", // No difference because FFF
		clause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	void testEqualEdgesInAreCongruentWithAllDifferentEdges() {
		Triangle triangle1 = new Triangle(3, 4, 5);
		Triangle triangle2 = new Triangle(3, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	/*
		15 - CC tests
	 */
	@ClauseCoverage(
		predicate = "a + b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false)
		}
	)
	@Test
	void testClauseCoverageFF() {
		Triangle triangle1 = new Triangle(3, 4, 5);
		Triangle triangle2 = new Triangle(3, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	@ClauseCoverage(
		predicate = "a + b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true)
		}
	)
	@Test
	void testClauseCoverageFT() {
		Triangle triangle1 = new Triangle(3, 4, 9);
		Triangle triangle2 = new Triangle(3, 4, 9);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	// TF is not possible

	@ClauseCoverage(
		predicate = "a + b",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = true)
		}
	)
	@Test
	void testClauseCoverageTT() {
		Triangle triangle1 = new Triangle(-1, 4, 9);
		Triangle triangle2 = new Triangle(-1, 4, 9);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/*
		15 - CACC
	 */

	// TF is not possible

	@CACC(
		predicate = "a + b",
		majorClause = 'a',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false)
		},
		predicateValue = true
	)
	@Test
	void testCACCFirstEdgeIsPositiveAndSSumIsGreater() {
		Triangle triangle1 = new Triangle(3, 4, 5);
		Triangle triangle2 = new Triangle(3, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	@CACC(
		predicate = "a + b",
		majorClause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true)
		},
		predicateValue = false
	)
	@Test
	void testCACCFirstEdgeIsPositiveAndSSumIsLower() {
		Triangle triangle1 = new Triangle(3, 4, 9);
		Triangle triangle2 = new Triangle(3, 4, 9);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@CACC(
		predicate = "a + b",
		majorClause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false)
		},
		predicateValue = true
	)
	@Test
	void testCACCFirstEdgeIsPositiveAndSSumIsGreater2() {
		Triangle triangle1 = new Triangle(3, 4, 5);
		Triangle triangle2 = new Triangle(3, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(triangle1, triangle2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	/**
	 * predicate = a'b + bc
	 * CUTPNFP implicants: {a'b, bc}
	 * CUTPNFP: {unique true points + near false points} =
	 * unique true points: {FTF, TTT}
	 * near false points: {TTF, FFF, FFT, TTF}
	 * CUTPNFP: {FTF, TTT, TTF, FFF, FFT}
	 *
	 * predicate = a'b + bc
	 * predicate' = (a | b') & (b' | c') = ac' + b'
	 * UTPC = {TTT, FTF, TFT, TTF , FFF, FFT}
	 *
	 * TFT is not included in CUTPNFP so it does not subsume.
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = false;
		predicate = (!a && b) || (b & c);
		return predicate;
	}
}
