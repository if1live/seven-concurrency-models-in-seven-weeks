defmodule Patterns do
  def foo({x, y}) do
    IO.puts("Got a pair, first element #{x}, second #{y}")
  end

  def foo({x, y, z}) do
    IO.puts("got a triple: #{x}, #{y}, #{z}")
  end
end
