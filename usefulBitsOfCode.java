	Reflections reflections = new Reflections("com.project.biddingSoft");
		
		Set<Class<? extends IStorable>> subTypes =	reflections.getSubTypesOf(IStorable.class);
		String str = subTypes.toArray()[2].toString(); 
		 String className = subTypes.toArray()[2].toString().substring(str.indexOf(' ') + 1);
		 Class<?> concrete = Class.forName(className);
		 
		  return (List<IStorable>) (List<?>) llots;
		  return  StreamSupport.stream(lots.spliterator(), false).collect(Collectors.toList());
		  return Collections.unmodifiableCollection(StreamSupport.stream(lots.spliterator(), false).collect(Collectors.toList()));
	