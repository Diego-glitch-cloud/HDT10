import math

CLIMAS = ['normal', 'lluvia', 'nieve', 'tormenta']

def init_graph():
    return {'cities': [], 'idx': {}, 'w': []}

def resize(graph):
    n = len(graph['cities'])
    old_n = len(graph['w'][0]) if graph['w'] else 0
    new_w = [[[math.inf] * n for _ in range(n)] for _ in CLIMAS]
    for c in range(len(CLIMAS)):
        for i in range(old_n):
            for j in range(old_n):
                new_w[c][i][j] = graph['w'][c][i][j]
        for i in range(n):
            new_w[c][i][i] = 0.0
    graph['w'] = new_w

def add_edge(graph, a, b, t):
    for v in (a, b):
        if v not in graph['idx']:
            graph['idx'][v] = len(graph['cities'])
            graph['cities'].append(v)
            resize(graph)
    i, j = graph['idx'][a], graph['idx'][b]
    for c, x in enumerate(t):
        graph['w'][c][i][j] = x

def remove_edge(graph, a, b):
    if a in graph['idx'] and b in graph['idx']:
        i, j = graph['idx'][a], graph['idx'][b]
        for c in range(len(CLIMAS)):
            graph['w'][c][i][j] = math.inf

def load_txt(path, graph):
    try:
        with open(path, encoding='utf-8') as f:
            for line in f:
                p = line.strip().split()
                if len(p) == 6:
                    try:
                        tiempos = list(map(float, p[2:]))
                        add_edge(graph, p[0], p[1], tiempos)
                    except ValueError:
                        pass
        return True
    except Exception:
        return False

def floyd(graph, clima_index):
    n = len(graph['cities'])
    d = [row[:] for row in graph['w'][clima_index]]
    nxt = [[j if d[i][j] < math.inf else None for j in range(n)] for i in range(n)]
    for k in range(n):
        for i in range(n):
            for j in range(n):
                if d[i][k] + d[k][j] < d[i][j]:
                    d[i][j] = d[i][k] + d[k][j]
                    nxt[i][j] = nxt[i][k]
    return d, nxt

def shortest_path(graph, nxt, frm, to):
    i = graph['idx'].get(frm)
    j = graph['idx'].get(to)
    if i is None or j is None or nxt[i][j] is None:
        return []
    path = [i]
    while i != j:
        i = nxt[i][j]
        path.append(i)
    return [graph['cities'][x] for x in path]

def center(graph, d):
    ecc = [max(row) for row in d]
    idx = ecc.index(min(ecc))
    return graph['cities'][idx]

def main():
    g = init_graph()
    if not load_txt('logistica.txt', g):
        print('Error cargando logistica.txt')
        return

    clima = 0
    d, nxt = floyd(g, clima)

    while True:
        op = input('1:Ruta 2:Centro 3:Mod 4:Clima 5:Salir> ')
        if op == '1':
            a = input('De: ').strip(); b = input('A: ').strip()
            path = shortest_path(g, nxt, a, b)
            dist = d[g['idx'].get(a, -1)][g['idx'].get(b, -1)] if path else math.inf
            print(dist, path or 'No hay ruta')
        elif op == '2':
            print(center(g, d))
        elif op == '3':
            t = input('interrumpir/agregar (i/a) conexión? ')
            # forma de uso: 
            # a (agregar)
            # BuenosAires (De:)
            # Lima (A: )
            # 15 22 35 75 (Tiempos: )
            a = input('De: ').strip(); b = input('A: ').strip()
            if t == 'i':
                remove_edge(g, a, b)
            else:
                times = list(map(float, input('Tiempos: ').split()))
                add_edge(g, a, b, times)
            d, nxt = floyd(g, clima)
        elif op == '4':
            clima = int(input(f'Clima {list(enumerate(CLIMAS))}: '))
            d, nxt = floyd(g, clima)
        else:
            break

if __name__ == '__main__':
    main()
