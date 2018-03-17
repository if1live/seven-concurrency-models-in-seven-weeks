# elixir demo.exs
# elixir 1.6.1

Code.require_file("cache3.ex")

CacheSupervisor.start_link

Cache.put("google.com", "hello-world-google")
Cache.put("twitter.com", "hello-world-twitter")

content = Cache.get("twitter.com")
IO.puts(content)

content = Cache.get("google.com")
IO.puts(content)

content = Cache.get("not-found.com")
IO.puts(content)
