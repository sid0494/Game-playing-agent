import numpy
import copy

class cell:

	def __init__(self, value, owner='.'):
		self.owner = owner
		self.value = value

def aplha_beta_decision(grid, values, players, N, depth, cutoff):

	move = None
	v = -9999
	aplha = -9999
	beta = 9999
	for s in possible_stakes(grid, players[0], N):
		new_value = min_value_ab(execute_stake(s[1], s[2], grid, players[0], N), values, players, N, depth + 1, cutoff, aplha, beta)
		if v < new_value:
			v = new_value
			move = [s[0] + " Stake", (s[1], s[2])]
			# print move
	for r in possible_raids(grid, players[0], N):
		new_value = min_value_ab(execute_raid(r[1], r[2], grid, players[0], N), values, players, N, depth + 1, cutoff, aplha, beta)
		if v < new_value:
			v = new_value
			move = [r[0] + " Raid", (r[1], r[2])]
			# print move

	return move

def min_value_ab(grid, values, players, N, depth, cutoff, aplha, beta):

	if depth == cutoff:
		return grid_value(grid, players[0], N)
	else:
		v = 9999
		for s in possible_stakes(grid, players[1], N):
			new_value = max_value_ab(execute_stake(s[1], s[2], grid, players[1], N), values, players, N, depth + 1, cutoff, aplha, beta)
			if v > new_value:
				v = new_value
				if v <= aplha:
					return v
				if beta > v:
					beta = v
		for r in possible_raids(grid, players[1], N):
			new_value = max_value_ab(execute_raid(r[1], r[2], grid, players[1], N), values, players, N, depth + 1, cutoff, aplha, beta)
			if v > new_value:
				v = new_value
				if v <= aplha:
					return v
				if beta > v:
					beta = v
		return v

def max_value_ab(grid, values, players, N, depth, cutoff, aplha, beta):

	if depth == cutoff:
		return grid_value(grid, values, players[0], N)
	else:
		v = -9999
		for s in possible_stakes(grid, players[0], N):
			new_value = min_value_ab(execute_stake(s[1], s[2], grid, players[0], N), values, players, N, depth + 1, cutoff, aplha, beta)
			if v < new_value:
				v = new_value
				if v >= beta:
					return v
				if aplha < v:
					alpha = v
		for r in possible_raids(grid, players[0], N):
			new_value = min_value_ab(execute_raid(r[1], r[2], grid, players[0], N), values, players, N, depth + 1, cutoff, aplha, beta)
			if v < new_value:
				v = new_value
				if v >= beta:
					return v
				if aplha < v:
					alpha = v
		return v

def minimax_decision(grid, values, players, N, depth, cutoff):

	move = None
	v = -9999
	for s in possible_stakes(grid, players[0], N):
		new_value = min_value(execute_stake(s[1], s[2], grid, players[0], N), values, players, N, depth + 1, cutoff)
		if v < new_value:
			v = new_value
			move = [s[0] + " Stake", (s[1], s[2])]
			# print move
	for r in possible_raids(grid, players[0], N):
		new_value = min_value(execute_raid(r[1], r[2], grid, players[0], N), values, players, N, depth + 1, cutoff)
		if v < new_value:
			v = new_value
			move = [r[0] + " Raid", (r[1], r[2])]
			# print move

	return move

def min_value(grid, values, players, N, depth, cutoff):

	if depth == cutoff:
		return grid_value(grid, players[0], N)
	else:
		v = 9999
		for s in possible_stakes(grid, players[1], N):
			new_value = max_value(execute_stake(s[1], s[2], grid, players[1], N), values, players, N, depth + 1, cutoff)
			if v > new_value:
				v = new_value
		for r in possible_raids(grid, players[1], N):
			new_value = max_value(execute_raid(r[1], r[2], grid, players[1], N), values, players, N, depth + 1, cutoff)
			if v > new_value:
				v = new_value
		return v

def max_value(grid, values, players, N, depth, cutoff):

	if depth == cutoff:
		return grid_value(grid, values, players[0], N)
	else:
		v = -9999
		for s in possible_stakes(grid, players[0], N):
			new_value = min_value(execute_stake(s[1], s[2], grid, players[0], N), values, players, N, depth + 1, cutoff)
			if v < new_value:
				v = new_value
		for r in possible_raids(grid, players[0], N):
			new_value = min_value(execute_raid(r[1], r[2], grid, players[0], N), values, players, N, depth + 1, cutoff)
			if v < new_value:
				v = new_value
		return v


def grid_value(grid, values, player, N):

	player_score = 0

	for r in xrange(0, N):
		for c in xrange(0, N):
			if grid[r][c] == player:
				player_score += values[r][c]
			elif grid[r][c] != '.':
				player_score -= values[r][c]

	return player_score

def possible_stakes(grid, player, N):

	stakes = []

	for r in xrange(0, N):
		for c in xrange(0, N):
			if grid[r][c] == '.':
				stakes.append((chr(ord('A') + c) + str(r + 1), r, c))

	return stakes

def possible_raids(grid, player, N):
	
	raids = []

	for r in xrange(0, N):
		for c in xrange(0, N):
			if grid[r][c] == player:
				raids += check_raids(r, c, grid, player, N)

	return raids

def check_raids(r, c, grid, player, N):
	
	raids = []

	for i in [-1, 1]:
		if r + i >= 0 and r + i < N:
			if grid[r + i][c] == '.':
				raids.append((chr(ord('A') + c) + str(r + i + 1), r + i, c))

	for j in [-1, 1]:
		if c + j >= 0 and c + j < N:
			if grid[r][c + j] == '.':
				raids.append((chr(ord('A') + c + j) + str(r + 1), r, c + j))

	return raids

def execute_stake(r, c, grid, player, N):
	
	new_grid = map(list, grid)
	new_grid[r][c] = player

	return new_grid

def execute_raid(r, c, grid, player, N):
	
	new_grid = map(list, grid)
	new_grid[r][c] = player

	for i in [-1, 1]:
		if r + i >= 0 and r + i < N:
			if grid[r + i][c] != '.':
				new_grid[r + i][c] = player

	for j in [-1, 1]:
		if c + j >= 0 and c + j < N:
			if grid[r][c + j] != '.':
				new_grid[r][c + j] = player

	return new_grid

def get_opponent(player):
	if player == 'X':
		return 'O'
	return 'X'


#-------------------------------------------------------------------------------------------
#-------------------------------------------------------------------------------------------

input_file = open("input.txt")
output_file = open("output.txt", "w")

N = int(input_file.readline().rstrip('\n'))
algorithm = input_file.readline().rstrip('\n')
player = input_file.readline().rstrip('\n')
cutoff = int(input_file.readline().rstrip('\n'))
values = []
grid = []

for r in xrange(0,N):
	data = input_file.readline().rstrip('\n').split(' ')
	row = [int(x) for x in data]
	# print row
	values.append(row)

for r in xrange(0,N):
	data = input_file.readline().rstrip('\n').split(' ')
	row = [x for x in data]
	grid.append(row)

players = [player, get_opponent(player)]

if algorithm == "MINIMAX":
	optimal_move = minimax_decision(grid, values, players, N, 0, cutoff)
elif algorithm == "ALPHABETA":
	optimal_move = aplha_beta_decision(grid, values, players, N, 0, cutoff)
else:
	#competition strategy
	print "Work in progress...."

# print optimal_move[0]

if "Raid" in optimal_move[0]:
	grid = execute_raid(optimal_move[1][0], optimal_move[1][1], grid, player, N)
else:
	grid = execute_stake(optimal_move[1][0], optimal_move[1][1], grid, player, N)



# #print possible_stakes(grid, player, N)
# raids = possible_raids(grid, player, N)

# new = execute_raid(raids[0][1], raids[0][2], grid, player, N)

output_file.write(optimal_move[0])

array = "\n"

for r in xrange(0,N):
	for c in xrange(0,N):
		array += grid[r][c]
		array += " "
	array += "\n"

output_file.write(array)
output_file.close()

# print ""

# for r in xrange(0,N):
# 	for c in xrange(0,N):
# 		print str(grid[r][c].value) + ' ' + new[r][c].owner,
# 	print ""