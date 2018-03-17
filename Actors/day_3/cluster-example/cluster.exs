Code.require_file("counter.ex")
pid = spawn(Counter, :loop, [42])
:global.register_name(:counter, pid)

IO.puts("cluster counter running...")
