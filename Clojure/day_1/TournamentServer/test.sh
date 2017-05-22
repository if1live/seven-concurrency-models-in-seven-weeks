#!/bin/bash

curl localhost:3000/players

curl -X put localhost:3000/players/john
curl localhost:3000/players

curl -X put localhost:3000/players/paul
curl -X put localhost:3000/players/george
curl -X put localhost:3000/players/ringo
curl localhost:3000/players
