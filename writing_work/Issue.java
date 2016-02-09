public static final Issue CHECKFRAGMENTACTIVITY = 
        Issue.create(
              "MainActivityIsFragmentActivity", // id
              "The main activity should extends FragmentActivity class", // descricao curta
              "Checks if the main activity defined in manifest file extends the FragmentActivity class", // descricao longa
              Category.CORRECTNESS, // categoria
              6, // prioridade
              Severity.WARNING, // severidade
              new Implementation(
                    PatternsDetector.class, // detector que procura por essa issue
               		EnumSet.of(Scope.ALL_JAVA_FILES, Scope.MANIFEST)) // escopo
              );

