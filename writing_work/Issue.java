public static final Issue FRAGMENT_ACTIVITY = Issue.create(
            "ActivityIsFragmentActivity", // id
            "The activities should extends "
                     + CLASS_V4_FRAGMENTACTIVITY + " class", // descricao curta
            "Checks if the activities extends the " 
                     + CLASS_V4_FRAGMENTACTIVITY + " class", // descricao longa
            Category.CORRECTNESS, 6, // categoria, prioridade 
            Severity.WARNING, // severidade
            new Implementation(
                     SuperClassDetector.class, // detector que procura por essa issue
                     EnumSet.of(Scope.ALL_JAVA_FILES)) // escopo
            );
