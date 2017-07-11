# 5.3.1절에서 만들었던 캐리를 다음과 같이 수정하라.
# 해시 함수를 이용해서 캐시 항목들이 여러 액터에 분산되도록 분산캐시를 작성하라.
# 여러 개의 캐시 액터를 생성하는 감시자를 만들고,
# 전달되는 메세지를 적절한 캐시 작업자에게 할당하도록 만들어라.
# 캐시 작업자 중 하나에서 에러가 발생되면 이 감시자는 어떤 동작을 수행해야 하는가?


defmodule DistCache do
  def start_link do
    spawn_link(__MODULE__, :loop, [HashDict.new, 0])
  end

  def loop(pages, size) do
    receive do
      {:put, url, page} ->
        new_pages = Dict.put(pages, url, page)
        new_size = size + byte_size(page)
        loop(new_pages, new_size)

      {:get, sender, ref, url} ->
        send(sender, {:ok, ref, pages[url]})
        loop(pages, size)

      {:size, sender, ref} ->
        send(sender, {:ok, ref, size})
        loop(pages, size)

      {:terminate} -> exit(:normal)
    end
  end

  def put(pid, url, page) do
    send(pid, {:put, url, page})
  end

  def get(pid, url) do
    ref = make_ref()
    send(pid, {:get, self(), ref, url})

    receive do
      {:ok, ^ref, page} -> page
    end
  end

  def size(pid) do
    ref = make_ref()
    send(pid, {:size, self(), ref})

    receive do
      {:ok, ^ref, s} -> s
    end
  end

  def terminate(pid) do
    send(pid, :terminate)
  end
end

defmodule DistCacheSupervisor do
  def start do
    spawn(__MODULE__, :loop_system, [])
  end

  def loop do
  end

  def loop_system do
    Process.flag(:trap_exit, true)
    loop
  end
end
