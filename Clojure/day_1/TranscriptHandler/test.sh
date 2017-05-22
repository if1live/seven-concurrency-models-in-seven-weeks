#!/bin/bash

# "Twas brillig, and the slithy toves Did gyre and gimble in the wabe:"
#   "Il brilgue, les tôves lubricilleux Se gyrent en vrillant dans le guave:"
# "All mimsy were the borogoves, And the mome raths outgrabe."
#   "Enmîmés sont les gougebosqueux Et le mômerade horsgrave."
# "Beware the Jabberwock, my son!"
#   "Garde-toi du Jaseroque, mon fils!"
# "The jaws that bite, the claws that catch!"
#   "La gueule qui mord; la griffe qui prend!"
# "Beware the Jubjub bird, and shun The frumious Bandersnatch!"
#   "Garde-toi de l'oiseau Jube, évite Le frumieux Band-à-prend!"


SID=$(curl -X post localhost:3000/session/create)
echo $SID

curl -X put -d "Twas brillig, and the slithy toves" -H "Content-Type: text/plain" localhost:3000/session/$SID/snippet/0
curl -X put -d "Did gyre and gimble in the wabe:" -H "Content-Type: text/plain" localhost:3000/session/$SID/snippet/1

curl -X put -d "All mimsy were the borogoves," -H "Content-Type: text/plain" localhost:3000/session/$SID/snippet/2
curl -X put -d "And the mome raths outgrabe." -H "Content-Type: text/plain" localhost:3000/session/$SID/snippet/3

echo ""
curl localhost:3000/session/$SID/translation/0
echo ""


echo ""
#curl localhost:3000/session/9999/translation/0
curl -sw '%{http_code}' localhost:3000/session/$SID/translation-safe/99
echo ""
