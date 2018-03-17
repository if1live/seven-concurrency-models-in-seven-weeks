defmodule Cache do
  # https://stackoverflow.com/a/43693815
  # use GenServer.Behaviour
  use GenServer

  def handle_cast({:put, url, page}, {pages, size}) do
    # new_pages = Dict.put(pages, url, page)
    new_pages = Map.put(pages, url, page)
    new_size = size + byte_size(page)
    {:noreply, {new_pages, new_size}}
  end

  def handle_call({:get, url}, _from, {pages, size}) do
    {:reply, pages[url], {pages, size}}
  end

  def handle_call({:size}, _from, {pages, size}) do
    {:reply, size, {pages, size}}
  end

  # warning: function init/1 required by behaviour GenServer is not implemented (in module Cache).
  def init(args) do
    {:ok, args}
  end


  def start_link do
    # warning: HashDict.new/0 is deprecated, use maps and the Map module instead
    # :gen_server.start_link({:local, :cache}, __MODULE__, {HashDict.new, 0}, [])
    :gen_server.start_link({:local, :cache}, __MODULE__, {Map.new, 0}, [])
  end

  def put(url, page) do
    :gen_server.cast(:cache, {:put, url, page})
  end

  def get(url) do
    :gen_server.call(:cache, {:get, url})
  end

  def size do
    :gen_server.call(:cache, {:size})
  end
end

defmodule CacheSupervisor do
  # use Supervisor 없으면 undefined function worker/2 에러 발생
  use Supervisor

  def init(_args) do
    workers = [worker(Cache, [])]
    supervise(workers, strategy: :one_for_one)
  end

  def start_link do
    :supervisor.start_link(__MODULE__, [])
  end
end
