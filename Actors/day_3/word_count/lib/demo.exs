Code.require_file("counter.ex")
Code.require_file("parser.ex")
Code.require_file("accumulator.ex")
Code.require_file("pages.ex")


AccumulatorSupervisor.start_link
ParserSupervisor.start_link("sample.xml")
CounterSupervisor.start_link(2)

Process.sleep(1000)
{totals, _pages} = Accumulator.get_results
IO.inspect(totals)
