# receive 블록에서 패턴 매치에 의해서 처리되지 않는 메세지는
# 프로세스의 메일박스에 남아있게 된다.
# 이 사실과 타임아웃을 이용해서
# 우선순위 메일박스 priority mailbox를 구현하라.
# 우선순위가 높은 메세지는 우선순위가 낮은 메세지보다 먼저 처리되어야 한다.

defmodule PriorityMailbox do
  def start_link do
    pid = spawn_link(__MODULE__, :loop, [])
    pid
  end

  def loop do
    pid = self()
    receive do
      {:push, msg, priority} ->
        send(pid, {:execute, msg})
        loop

      {:execute, msg} ->
        IO.puts("#{inspect msg}")
        loop

      {:shutdown} ->
        IO.puts("shutdown")
        exit(:normal)

    after 100 ->
        IO.puts("timeout")

    end
  end

  def push(pid, msg, priority) do
    send(pid, {:push, msg, priority})
  end

  def dummy(pid, msg, priority) do
    send(pid, {:unknown, msg, priority})
  end

  def shutdown(pid) do
    send(pid, {:shutdown})
  end
end
