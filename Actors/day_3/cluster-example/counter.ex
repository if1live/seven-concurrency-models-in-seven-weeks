defmodule Counter do
  def loop(count) do
    receive do
      {:next} ->
        IO.puts("Current count: #{count}")
        loop(count+1)
    end
  end
end

# counter = spawn(Counter, :loop, [1])
# send(counter, {:next})
# send(counter, {:next})
# send(counter, {:next})

# execute order

# 1. setup cluster for counter
# iex --sname node2 cluster.exs

# 2. run demo
# iex --sname node1
# demo.exs 를 수동으로 입력
# 스크립트로 즉시 실행하니 노드 연결이 바로 안붙는거같다
