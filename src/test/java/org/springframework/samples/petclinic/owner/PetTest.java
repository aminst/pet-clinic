package org.springframework.samples.petclinic.owner;


import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.visit.Visit;
import org.thymeleaf.util.ArrayUtils;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@RunWith(Theories.class)
public class PetTest {

	@DataPoints
	public static List[] visits1 = {
		Arrays.asList(
			new Visit().setDate(LocalDate.of(2021, 10, 13)),
			new Visit().setDate(LocalDate.of(2015, 8, 12))
		),
		Arrays.asList(
			new Visit().setDate(LocalDate.of(2000, 3, 2))
		)
	};

	@DataPoints
	public static List[] visits2 = {
		Arrays.asList(
			new Visit().setDate(LocalDate.of(2018, 10, 11)),
			new Visit().setDate(LocalDate.of(1983, 10, 20))
		),
		Arrays.asList(
			new Visit().setDate(LocalDate.of(1983, 10, 23)),
			new Visit().setDate(LocalDate.of(2018, 9, 10))
		),
		Arrays.asList(new Visit().setDate(LocalDate.of(1999, 2, 3)))
	};

	@Theory
	public void testGetVisitsReturnsCorrectlyOrderedVisits(List<Visit> visits1, List<Visit> visits2) {
		assumeTrue(visits1.size() != 0 && visits2.size() != 0);

		Pet pet = new Pet();
		for (Visit visit : visits1)
			pet.addVisit(visit);
		for (Visit visit : visits2)
			pet.addVisit(visit);

		List<Visit> returnedVisits = pet.getVisits();

		List<Visit> sortedVisits = new ArrayList<Visit>(returnedVisits);
		PropertyComparator.sort(sortedVisits, new MutableSortDefinition("date", false, false));
		assertArrayEquals("getVisits did not return visits in the correct order", sortedVisits.toArray(), returnedVisits.toArray());
	}

	@Theory
	public void testGetVisitsReturnsCorrectValues(List<Visit> visits1, List<Visit> visits2) {
		assumeTrue(visits1.size() != 0 && visits2.size() != 0);

		Pet pet = new Pet();
		for (Visit visit : visits1)
			pet.addVisit(visit);
		for (Visit visit : visits2)
			pet.addVisit(visit);

		List<Visit> returnedVisits = pet.getVisits();

		for (Visit visit : visits1)
			assertTrue(String.format("getVisits did not contain visit with date %s", visit.toString()), ArrayUtils.contains(returnedVisits.toArray(), visit));
		for (Visit visit : visits2)
			assertTrue(String.format("getVisits did not contain visit with date %s", visit.toString()), ArrayUtils.contains(returnedVisits.toArray(), visit));
	}
}
