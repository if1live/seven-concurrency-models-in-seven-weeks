Code.require_file "dist_cache.ex"

pid1 = DistCache.start_link
DistCache.put(pid1, "google.com", "google content")
IO.puts(DistCache.get(pid1, "google.com"))
IO.puts(DistCache.size(pid1))

pid2 = DistCache.start_link
DistCache.put(pid2, "amazon.com", "amazon content")
IO.puts(DistCache.get(pid2, "amazon.com"))
IO.puts(DistCache.size(pid2))
