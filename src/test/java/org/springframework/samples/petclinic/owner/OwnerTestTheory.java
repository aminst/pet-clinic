package org.springframework.samples.petclinic.owner;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.assertSame;
import static org.junit.Assume.assumeTrue;

@RunWith(Theories.class)
public class OwnerTestTheory {

	@DataPoints
	public static String[] petNames = {
		"lovely cat",
		"annoying dog",
		"random name",
		"panda1",
		"nice kangaroo",
		"a",
		"d",
		"e",
		"zz",
		"z",
		"test"
	};

	@DataPoints
	public static List<List<String>> petNamesList() {

		List<List<String>> pets = new ArrayList<>();
		pets.add(Arrays.asList("lovely cat", "annoying dog", "random name"));
		pets.add(Arrays.asList("panda1", "nice kangaroo"));
		pets.add(Arrays.asList("a", "d", "e", "zz"));
		pets.add(Arrays.asList("z", "test"));
		return pets;
	}

	@DataPoints
	public static boolean[] ignoreNew = {true, false};

	static Owner setUpOwner(String petName, List<String> petNames) {
		Owner owner = new Owner();
		for (String newPetName : petNames) {
			if (newPetName != petName) {
				Pet newPet = new Pet();
				newPet.setName(newPetName);
				owner.addPet(newPet);
			}
		}
		return owner;
	}


	@Theory
	public void testGetPetFindsExistingPet(String petName, List<String> petNames) {
		assumeTrue(petNames.size() != 0 && petName != null);
		assumeTrue(petNames.contains(petName));

		Owner owner = setUpOwner(petName, petNames);
		Pet toBeFoundPet = new Pet();
		toBeFoundPet.setName(petName);
		owner.addPet(toBeFoundPet);

		assertSame("getPet did not find existing pet", toBeFoundPet, owner.getPet(petName));
	}

	@Theory
	public void testGetPetReturnsNullForNonExistingPet(String petName, List<String> petNames) {
		assumeTrue(petNames.size() != 0 && petName != null);
		assumeTrue(!petNames.contains(petName));

		Owner owner = setUpOwner(petName, petNames);

		assertSame("getPet found not existing pet", null, owner.getPet(petName));

	}

	@Theory
	public void testGetPetWithIgnoreNewTrueIgnoresNewPets(String petName, List<String> petNames, boolean ignoreNew) {
		assumeTrue(petNames.size() != 0 && petName != null);
		assumeTrue(petNames.contains(petName));
		assumeTrue(ignoreNew == true);

		Owner owner = setUpOwner(petName, petNames);
		Pet toBeFoundPet = new Pet();
		toBeFoundPet.setName(petName);
		toBeFoundPet.setId(null);
		owner.addPet(toBeFoundPet);

		assertSame("getPet did not ignore new pet", null, owner.getPet(petName, ignoreNew));
	}

	@Theory
	public void testGetPetsWithIgnoreNewTrueDoesNotIgnoreOldPets(String petName, List<String> petNames, boolean ignoreNew) {
		assumeTrue(petNames.size() != 0 && petName != null);
		assumeTrue(petNames.contains(petName));
		assumeTrue(ignoreNew == true);

		Owner owner = setUpOwner(petName, petNames);

		Pet toBeFoundPet = new Pet();
		owner.addPet(toBeFoundPet);
		toBeFoundPet.setName(petName);
		toBeFoundPet.setId(1379);

		assertSame("getPet did ignored old pet", toBeFoundPet, owner.getPet(petName, ignoreNew));
	}

	@Theory
	public void testGetPetIsCaseInsensitive(String petName, List<String> petNames, boolean ignoreNew) {
		assumeTrue(petNames.size() != 0 && petName != null);
		assumeTrue(petNames.contains(petName));

		Owner owner = setUpOwner(petName, petNames);
		Pet toBeFoundPet = new Pet();
		toBeFoundPet.setName(petName);
		owner.addPet(toBeFoundPet);

		assertSame("getPet is not case insensitive", toBeFoundPet, owner.getPet(petName.toUpperCase()));
	}
}
