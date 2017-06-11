# 우리가 만든 병렬적 맵과 비슷한 방식을 활용해서
# 병렬적 축소 함수 recude function을 작성하라.
defmodule Parallel do
  def reduce(collection, acc, fun) do
    parent = self()

    # 1. N개의 조각으로 collection을 나눈다
    # 2. 각각의 조각에 대해 reduce를 수행
    # 3. 조각의 결과물을 모아서 reduce 수행
    # 제한사항 : acc가 항등원일때만 올바르게 동작한다
    # collection을 여러개로 쪼갠후 각각에 대해서 reduce를 수행하기때문에 서로 독립적이어야한다
    # acc가 항등원이면 각각의 reduce는 독립적으로 수행후 마지막에 합쳐도 올바른 결과가 나올것이다

    # https://stackoverflow.com/questions/36818101/how-to-get-concurrent-function-pmap-to-use-all-cores-in-elixir
    multiplier = 1
    core_count = :erlang.system_info(:schedulers_online)
    process_count = multiplier * core_count

    processes = Enum.map(0..process_count-1, fn(x) ->
      elems = get_sliced_collection(collection, process_count, x)
      spawn_link(fn() ->
        send(parent, {self(), Enum.reduce(elems, acc, fun)})
      end)
    end)

    outputs = Enum.map(processes, fn(pid) ->
      receive do
        {^pid, result} -> result
      end
    end)

    Enum.reduce(outputs, acc, fun)
  end

  def get_sliced_collection(collection, total_count, idx) do
    sub_length = div(length(collection), total_count)
    start_idx = sub_length * idx
    end_idx = if idx < total_count-1 do sub_length * (idx+1) else length(collection) end
    required_length = end_idx - start_idx
    #IO.puts("start=#{start_idx} len=#{required_length}")
    Enum.slice(collection, start_idx, required_length)
  end
end

# sample code
data_list = [1,2,3,4,5,6,7,8,9,10]
product = fn(x, acc) -> x * acc end
IO.puts("normal output : #{Enum.reduce(data_list, 1, product)}")
IO.puts("parallel output : #{Parallel.reduce(data_list, 1, product)}")

slow_product = fn(x, acc) -> :timer.sleep(500); product.(x, acc) end
normal = :timer.tc(fn() -> Enum.reduce(data_list, 1, slow_product) end)
parallel = :timer.tc(fn() -> Parallel.reduce(data_list, 1, slow_product) end)

IO.puts("normal : #{elem(normal, 0)}")
IO.puts("parallel : #{elem(parallel, 0)}")
