Code.require_file "patterns.ex"

Patterns.foo({:a, 42})
Patterns.foo({:a, 42, "yahoo"})
Patterns.foo("something else")
