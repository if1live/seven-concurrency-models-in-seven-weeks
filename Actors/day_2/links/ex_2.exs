Code.require_file("links.ex")

pid1 = spawn(&LinkTest.loop/0)
pid2 = spawn(&LinkTest.loop/0)
send(pid1, {:link_to, pid2})
send(pid1, {:exit_because, :bad_thing_happened})
Process.sleep(100)

pid1_status = Process.info(pid1, :status)
pid2_status = Process.info(pid2, :status)
IO.puts("pid1 = #{inspect pid1_status}")
IO.puts("pid2 = #{inspect pid2_status}")
