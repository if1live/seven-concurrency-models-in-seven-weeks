Code.require_file("cache.ex")

Cache.start_link
Cache.put("google.com", "Welcome to Google ...")
content = Cache.get("google.com")
IO.puts(content)
IO.puts(Cache.size())
Cache.put("paulbutcher.com", nil)
