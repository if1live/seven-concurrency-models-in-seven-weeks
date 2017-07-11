Code.require_file "mailbox.ex"

Process.flag(:trap_exit, true)
pid = PriorityMailbox.start_link

PriorityMailbox.push(pid, "spam", 300)
PriorityMailbox.push(pid, "bar", 200)
PriorityMailbox.push(pid, "foo", 100)
PriorityMailbox.dummy(pid, "dummy", 100)

Process.sleep(1000)
PriorityMailbox.shutdown(pid)
receive do
  {:EXIT, ^pid, reason} -> IO.puts("Mailbox has exited (#{reason})")
end
