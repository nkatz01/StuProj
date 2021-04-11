	Reflections reflections = new Reflections("com.project.biddingSoft");
		
		Set<Class<? extends IStorable>> subTypes =	reflections.getSubTypesOf(IStorable.class);
		String str = subTypes.toArray()[2].toString(); 
		 String className = subTypes.toArray()[2].toString().substring(str.indexOf(' ') + 1);
		 Class<?> concrete = Class.forName(className);
		 
		  return (List<IStorable>) (List<?>) llots;
		  return  StreamSupport.stream(lots.spliterator(), false).collect(Collectors.toList());
		  return Collections.unmodifiableCollection(StreamSupport.stream(lots.spliterator(), false).collect(Collectors.toList()));
	
	@Test
public void whenParameterCat_thenOnlyCatsFed() {
    List<Animal> animals = new ArrayList<>();
    animals.add(new Cat());
    animals.add(new Dog());
    AnimalFeederGeneric<Cat> catFeeder
      = new AnimalFeederGeneric<Cat>(Cat.class);
    List<Cat> fedAnimals = catFeeder.feed(animals);

    assertTrue(fedAnimals.size() == 1);
    assertTrue(fedAnimals.get(0) instanceof Cat);
}

//	 @PostMapping(path="/addlot") 
//	ResponseEntity<String> addNewLot(@RequestParam String userName ) {
//		 
//		 try {
//		    daoServiceImpl.persistEntity(userName);
//		 }
//			catch (Exception e) {
//				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//			}
//			
//		 return ResponseEntity.status(HttpStatus.CREATED).build();
//		   
//			
//				 
//	 }

@DisplayName("given order with two line items, when persist, then order is saved")

		return  Optional.of(this.lotList.stream().filter(l -> l.equals(lot)).findFirst()).orElse(  Optional.empty() );

		return  this.lotList.stream().filter(l -> l.equals(lot)).findFirst().or(() -> Optional.empty());

	private  ZoneId ZONE = ZoneId.of(System.getProperty("user.timezone")) ; 
	// 	System.out.println(Instant.now(Clock.system(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(+10)))));
//	 System.out.println(Instant.now(Clock. system(ZoneId.ofOffset("Europe/London", ZoneOffset.ofHours(+2)))));

@Test
public void givenPersonEntity_whenInsertedTwiceWithNativeQuery_thenPersistenceExceptionExceptionIsThrown() {
    Person person = new Person(1L, "firstname", "lastname");

    assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> {
        personInsertRepository.insertWithQuery(PERSON);
        personInsertRepository.insertWithQuery(PERSON);
    });
}

//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//		Query query = entityManager.createQuery("SELECT u FROM user u INNER JOIN u.id l where l.user_id = u.id");
//		Collection<User> users = query.getResultList();
		//assertTrue(users.size()>0);
		//Long id = users.stream().findFirst().get().getId();