Node.connect(:"node2@localhost")

pid = :global.whereis_name(:counter)
send(pid, {:next})
send(pid, {:next})
