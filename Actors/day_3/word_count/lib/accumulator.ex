defmodule Accumulator do
  use GenServer

  def start_link do
    :gen_server.start_link({:global, :wc_accumulator}, __MODULE__, {Map.new, MapSet.new}, [])
  end

  def init(args) do
    {:ok, args}
  end

  def delivery_counts(ref, counts) do
    :gen_server.cast({:global, :wc_accumulator}, {:delivery_counts, ref, counts})
  end

  def get_results do
    :gen_server.call({:global, :wc_accumulator}, {:get_result})
  end

  def handle_cast({:delivery_counts, ref, counts}, {totals, processed_pages}) do
    if MapSet.member?(processed_pages, ref) do
      {:noreply, {totals, processed_pages}}
    else
      new_totals = Map.merge(totals, counts, fn(_k, v1, v2) -> v1 + v2 end)
      new_processed_pages = MapSet.put(processed_pages, ref)
      Parser.processed(ref)
      {:noreply, {new_totals, new_processed_pages}}
    end
  end

  def handle_call({:get_result}, _, {totals, processed_pages}) do
    {:reply, {totals, processed_pages}, {totals, processed_pages}}
  end
end

defmodule AccumulatorSupervisor do
  use Supervisor

  def start_link do
    :supervisor.start_link(__MODULE__, [])
  end

  def init(_args) do
    workers = [worker(Accumulator, [])]
    supervise(workers, strategy: :one_for_one)
  end
end
