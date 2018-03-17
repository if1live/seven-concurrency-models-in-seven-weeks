defmodule Parser do
  use GenServer

  def start_link(filename) do
    :gen_server.start_link({:global, :wc_parser}, __MODULE__, filename, [])
  end

  def request_page(pid) do
    :gen_server.cast({:global, :wc_parser}, {:request_page, pid})
  end

  def processed(ref) do
    :gen_server.cast({:global, :wc_parser}, {:processed, ref})
  end

  def init(filename) do
    xml_parser = Pages.start_link(filename)
    {:ok, {Map.new, xml_parser}}
  end

  def handle_cast({:request_page, pid}, {pending, xml_parser}) do
    new_pending = delivery_page(pid, pending, Pages.next(xml_parser))
    {:noreply, {new_pending, xml_parser}}
  end

  def handle_cast({:processed, ref}, {pending, xml_parser}) do
    new_pending = Map.delete(pending, ref)
    {:noreply, {new_pending, xml_parser}}
  end

  defp delivery_page(pid, pending, page) when is_nil(page) do
    if Enum.empty?(pending) do
      pending
    else
      ref = List.last(Map.keys(pending))
      prev_page = Map.get(pending, ref)
      Counter.delivery_page(pid, ref, prev_page)
      Map.put(Map.delete(pending, ref), ref, prev_page)
    end
  end

  defp delivery_page(pid, pending, page) do
    ref = make_ref()
    Counter.delivery_page(pid, ref, page)
    Map.put(pending, ref, page)
  end
end


defmodule ParserSupervisor do
  use Supervisor
  def start_link(filename) do
    :supervisor.start_link(__MODULE__, filename)
  end

  def init(filename) do
    workers = [worker(Parser, [filename])]
    supervise(workers, strategy: :one_for_one)
  end
end
