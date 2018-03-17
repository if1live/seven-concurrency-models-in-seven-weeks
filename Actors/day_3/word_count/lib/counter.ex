defmodule Counter do
  use GenServer

  def start_link do
   :gen_server.start_link(__MODULE__, nil, [])
  end

  def delivery_page(pid, ref, page) do
    :gen_server.cast(pid, {:delivery_page, ref, page})
  end

  def init(_args) do
    Parser.request_page(self())
    {:ok, nil}
  end

  def handle_cast({:delivery_page, ref, page}, state) do
    Parser.request_page(self())

    words = String.split(page)
    counts = Enum.reduce(words, Map.new, fn(word, counts) ->
      Map.update(counts, word, 1, &(&1 + 1))
    end)
    Accumulator.delivery_counts(ref, counts)
    {:noreply, state}
  end
end


defmodule CounterSupervisor do
  use Supervisor

  def start_link(num_counters) do
    :supervisor.start_link(__MODULE__, num_counters)
  end
  def init(num_counters) do
    workers = Enum.map(1..num_counters, fn(n) ->
      worker(Counter, [], id: "counter#{n}")
    end)
    supervise(workers, strategy: :one_for_one)
  end
end
