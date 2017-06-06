#!/bin/bash


# curl http://localhost:3000/players
# curl -X PUT http://localhost:3000/players/foo
# curl -X PUT http://localhost:3000/players/bar
# curl http://localhost:3000/players
# echo ""

# 1~3 을 인덱스로 사용
# 1P:o, 2P:x
# --o
# xox
# oxo

function show_board() {
	curl http://localhost:3000/games/show/board-1
	echo ""
}

# 게임 생성
curl -X PUT http://localhost:3000/games/new/board-1/foo/bar
echo ""

show_board


# 1P, 2,2에 배치
curl -X PUT http://localhost:3000/games/play/board-1/foo/2/2
echo ""

show_board

# 1P, 2,1에 배치 -> 자신의 차례가 아니라서 실패
curl -X PUT http://localhost:3000/games/play/board-1/foo/2/1
echo ""
show_board


# 2P, 2,2에 배치 -> 이미 있는 지역이라서 실패
curl -X PUT http://localhost:3000/games/play/board-1/bar/2/2
echo ""
show_board
