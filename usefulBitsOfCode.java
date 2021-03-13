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