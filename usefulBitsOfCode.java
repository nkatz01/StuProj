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
		
		SELECT user.id FROM biddingsoft.user join biddingsoft.lot  on lot.user_id = user.id ;
		
		
		//spring.jpa.properties.hibernate.format_sql=true
		
			//JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
		//Lot lot =   new Gson().fromJson(EntityUtils.toString(response.getEntity()), Lot.class);
		Lot lot = new ObjectMapper()
				//.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				
					<repositories>
	<repository>
    <id>com.springsource.repository.bundles.release</id>
    <name>SpringSource Enterprise Bundle Repository - SpringSource Bundle Releases</name>
    <url>http://repository.springsource.com/maven/bundles/release</url>
</repository>
<repository>
    <id>com.springsource.repository.bundles.external</id>
    <name>SpringSource Enterprise Bundle Repository - External Bundle Releases</name>
    <url>http://repository.springsource.com/maven/bundles/external</url>
</repository>
<repository>
    <id>maven2</id>
    <url>http://repo1.maven.org/maven2</url>
</repository>
<repository>
    <id>org.springframework.maven.release</id>
    <name>Spring Maven Release Repository</name>
    <url>http://repo.springsource.org/libs-release-local</url>
    <releases><enabled>true</enabled></releases>
    <snapshots><enabled>false</enabled></snapshots>
</repository>
<!-- For testing against latest Spring snapshots -->
<repository>
    <id>org.springframework.maven.snapshot</id>
    <name>Spring Maven Snapshot Repository</name>
    <url>http://repo.springsource.org/libs-snapshot-local</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
<!-- For developing against latest Spring milestones -->
<repository>
    <id>org.springframework.maven.milestone</id>
    <name>Spring Maven Milestone Repository</name>
    <url>http://repo.springsource.org/libs-milestone-local</url>
    <snapshots><enabled>false</enabled></snapshots>
</repository>
</repositories>
				
				Field field =	user2.getClass().getSuperclass().getDeclaredField("businessId");
		field.setAccessible(true);
		field.set(user2, strblService.newUUID());
