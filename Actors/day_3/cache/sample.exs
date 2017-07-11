Code.require_file "cache3.ex"

CacheSupervisor.start_link
# Cache가 생성될때까지 잠시 기다린다
Process.sleep(10)

Cache.put("google.com", "Welcome to Google ...")
content = Cache.get("google.com")
IO.puts(content)
IO.puts(Cache.size)

Cache.put("paulbutcher.com", nil)
Process.sleep(100)

Cache.put("google.com", "Welcome to Google ...")
content = Cache.get("google.com")
IO.puts(content)
IO.puts(Cache.size)

Process.sleep(10)
