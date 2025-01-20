# python3

import sys
from collections import namedtuple

Answer = namedtuple('answer_type', 'i j len')

def HashValue(s, p, x):
    hashvalue = 0
    for i in range(len(s) - 1, -1, -1):
        hashvalue = (hashvalue * x + ord(s[i])) % p
    return hashvalue


def HashTable(s, p_len, prime, x):
    H = list([] for _ in range(len(s) - p_len + 1))
    substring = s[len(s) - p_len:]
    H[len(s) - p_len] = HashValue(substring, prime, x)
    y = pow(x, p_len, prime)
    for i in range(len(s) - p_len - 1, - 1, - 1):
        H[i] = (x * H[i + 1] + ord(s[i]) - y * ord(s[i + p_len])) % prime
    return H


def HashDictionary(s, p_len, prime, x):
    D = {}
    substring = s[len(s) - p_len:]
    last = HashValue(substring, prime, x)
    D[last] = len(s) - p_len
    y = pow(x, p_len, prime)
    for j in range(len(s) - p_len - 1, - 1, - 1):
        current = (x * last + ord(s[j]) - y * ord(s[j + p_len])) % prime
        D[current] = j
        last = current
    return D


def SearchSubstring(hash_table, hash_dict):
    check = False
    matches = {}
    for i in range(len(hash_table)):
        b_start = hash_dict.get(hash_table[i], -1)
        if b_start != -1:
            check = True
            matches[i] = b_start
    return check, matches


def MaxLength(string_s, string_t, low, high, max_length, sStart, tStart):
    mid = (low + high) // 2 
    if low > high:
        return sStart, tStart, max_length
    prime1 = 1000000007
    prime2 = 1000004249
    x = 263
    sHashFirst = HashTable(string_s, mid, prime1, x)
    sHashSecond = HashTable(string_s, mid, prime2, x)
    tHashFirst = HashDictionary(string_t, mid, prime1, x)
    tHashSecond = HashDictionary(string_t, mid, prime2, x)
    value1, match1 = SearchSubstring(sHashFirst, tHashFirst)
    value2, match2 = SearchSubstring(sHashSecond, tHashSecond)
    if value1 and value2:
        for a, b in match1.items():
            temp = match2.get(a, -1)
            if temp != -1:
                max_length = mid
                sStart, tStart = a, b
                del sHashFirst, sHashSecond, tHashFirst, tHashSecond, match1, match2
                return MaxLength(string_s, string_t, mid + 1, high, max_length, sStart, tStart)
    return MaxLength(string_s, string_t, low, mid - 1, max_length, sStart, tStart)

while True:
    line = input()
    if line == '':
        break
    else:
        s, t = line.split()
        k = min(len(s), len(t))
        if len(s) <= len(t):
            s_string, l_string = s, t
        else:
            s_string, l_string = t, s
        l, i, j = MaxLength(l_string, s_string, 0, k, 0, 0, 0)
        if len(s) <= len(t):
            print(i, l, j)
        else:
            print(l, i, j)
