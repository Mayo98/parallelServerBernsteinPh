#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Oct  1 12:07:30 2024

@author: giacomomagistrato
"""

import matplotlib.pyplot as plt

# Nome del file contenente i dati

#MODE = 1 AVAILABILITY ---- MODE = 2 PERFORMABILITY
mode = 1
filename = 'SteadyStateTests'
#filenamesSources = 'SteadyStateResults/Performability/ + filename +'.txt'
#filenamesSources = 'SteadyStateResults/Availability/P1 Tests/' + filename + '.txt'
#filenamesSources = 'SteadyStateResults/Availability/P2 Tests/' + filename + '.txt'
#filenamesSources = 'SteadyStateResults/Availability/P3 Tests/' + filename + '.txt'
filenamesSources = 'SteadyStateResults/Availability/P3 Tests/' + filename + '.txt'


#filenamesSources = 'SteadyStateResults/Performability/ + filename +'.txt'
#filenamesSources = 'SteadyStateResults/Performability/P3 Tests/' + filename + '.txt'
#filenamesSources = 'SteadyStateResults/Performability/P2 Tests/' + filename + '.txt'
#filenamesSources = 'SteadyStateResults/Performability/P3 Tests/' + filename + '.txt'
#filenamesSources = 'SteadyStateResults/Performability/Pool Tests/' + filename + '.txt'

# Elaborazione dei dati



if mode == 1:

    data_dict = { 'Ph1': [], 'Ph2': [], 'Ph3': [], 'Ph4': [], 'Pool': [] }
    with open(filenamesSources, 'r') as file:
        lines = file.readlines()

        for line in lines:
            line2 = line.strip()
            if line2 != '':
                parts = line.split(':')

                key = parts[0].strip()
                value, x = map(float, parts[1].strip().split())
                data_dict[key].append((value, int(x)))

    # Creazione del grafico
    plt.figure(figsize=(12, 5))

    for key in data_dict:
        #if key != 'Pool':  # Escludi "Pool"
        values, xs = zip(*data_dict[key])
        plt.plot(xs, values , marker='', label=key)

    # Aggiunta di etichette e titolo
    plt.xlabel('Valori')
    plt.ylabel('Probabilità')
    plt.title('Grafico delle Curve - Incremento di P3')
    plt.legend()
    plt.grid(True)

    #plt.tight_layout()
    plt.savefig('plots/SteadyState/' + filename +' -AvailabilityP3' +  '.png')
    plt.show()




if mode ==2:
    data_dict = {}

    with open(filenamesSources, 'r') as file:
        lines = file.readlines()

        for line in lines:
            line2 = line.strip()
            if line2 != '':
                parts = line.split(':')

                # Recupera la chiave e i valori
                key = parts[0].strip()
                value, x = map(float, parts[1].strip().split())

                # Usa setdefault per aggiungere la chiave se non esiste
                data_dict.setdefault(key, []).append((value, int(x)))

    # Creazione del grafico
    plt.figure(figsize=(13, 5))

    for key in data_dict:
        #if key != 'Pool':  # Escludi "Pool"
        values, xs = zip(*data_dict[key])
        plt.plot(xs, values , marker='', label=key)

    # Aggiunta di etichette e titolo
    plt.xlabel('Valori')
    plt.ylabel('Probabilità')
    plt.title('Incremento di P3')
    plt.legend()
    plt.grid(True)


    #plt.tight_layout()

    plt.savefig('plots/SteadyState/' + filename +' -PerformabilityP3' + '.png')
    plt.show()