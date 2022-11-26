import matplotlib.pyplot as plt
from matplotlib import cm
import numpy as np
from math import log10

# async
results_async_list = []
with open("results2_async.txt", "r") as file:
    lines = file.readlines()
    lines = [line.strip().split(" ") for line in lines if line != ""]

    while True:
        if len(lines) == 0:
            break
        tmp = []
        for i in range(10):
            tmp.append(lines.pop(0))
        arr = np.array(tmp, dtype=np.double)

        means = np.mean(arr, axis=0)
        devs = np.std(arr, axis=0)

        result = [i for i in zip(means, devs)]
        result[0] = int(result[0][0])
        result[1] = int(result[1][0])
        result[2] = int(result[2][0])
        # print(result)
        results_async_list.append(result)

# print(results_async_list)

# sync
results_sync_list = []
with open("results2_sync.txt", "r") as file:
    lines = file.readlines()
    lines = [line.strip().split(" ") for line in lines if line != ""]

    # print(lines[:10])

    while True:
        if len(lines) == 0:
            break
        tmp = []
        for i in range(10):
            tmp.append(lines.pop(0))
        arr = np.array(tmp, dtype=np.double)

        means = np.mean(arr, axis=0)
        devs = np.std(arr, axis=0)

        result = [i for i in zip(means, devs)]
        result[0] = int(result[0][0])
        result[1] = int(result[1][0])
        result[2] = int(result[2][0])
        # print(result)
        results_sync_list.append(result)


totalThreads = 2
z_min, z_max = 0, 40
my_cmap = cm.coolwarm
fig, ax = plt.subplots(1, 3, figsize=(15, 5), subplot_kw={'projection': '3d'})
ax1, ax2, ax3 = ax
# ax1 = fig.add_subplot(131, projection='3d')
# ax2 = fig.add_subplot(132, projection='3d')
# ax3 = fig.add_subplot(133, projection='3d')


width = depth = 1
x = [ log10(object_work) for threads, object_work, agent_work, _, _, _ in results_async_list if threads == totalThreads]
y = [ log10(agent_work) for threads, object_work, agent_work, _, _, _ in results_async_list if threads == totalThreads]
a_top = [ time[0] for threads, object_work, agent_work, time, _, _ in results_async_list if threads == totalThreads]
z = np.array(a_top).reshape((len(set(x)), len(set(y))))
bottom = np.zeros_like(a_top)


ax1.plot_surface(np.array(x).reshape((len(set(x)), len(set(y)))), np.array(y).reshape((len(set(x)), len(set(y)))), z, cmap=my_cmap)
# ax1.bar3d(x, y, bottom, width, depth, a_top, shade=True)
ax1.set_title(f'Czas wykonania programu asynchronicznego\n przy {totalThreads} wątkach')
ax1.set_xlabel('log10 z pracy na buforze')
ax1.set_ylabel('log10 z pracy agentów')
ax1.set_zlabel('Czas [s]')
ax1.view_init(30, -135)
ax1.set_zlim(z_min, z_max)

#######################################################################################

a_top = [ time[0] for threads, object_work, agent_work, time, _, _ in results_sync_list if threads == totalThreads]
z = np.array(a_top).reshape((len(set(x)), len(set(y))))

ax2.plot_surface(np.array(x).reshape((len(set(x)), len(set(y)))), np.array(y).reshape((len(set(x)), len(set(y)))), z, cmap=my_cmap)
# ax2.bar3d(x, y, bottom, width, depth, a_top, shade=True)
ax2.set_title(f'Czas wykonania programu synchronicznego\n przy {totalThreads} wątkach')
ax2.set_xlabel('log10 z pracy na buforze')
ax2.set_ylabel('log10 z praca agentów')
ax2.set_zlabel('Czas [s]')
ax2.view_init(30, -135)
ax2.set_zlim(z_min, z_max)

#######################################################################################

times_async = [ time[0] for threads, object_work, agent_work, time, _, _ in results_async_list if threads == totalThreads]
times_sync = [ time[0] for threads, object_work, agent_work, time, _, _ in results_sync_list if threads == totalThreads]
a_top = [ asy - sy for asy, sy in zip(times_async, times_sync) ]
z = np.array(a_top).reshape((len(set(x)), len(set(y))))
bottom = np.zeros_like(a_top)

ax3.plot_surface(np.array(x).reshape((len(set(x)), len(set(y)))), np.array(y).reshape((len(set(x)), len(set(y)))), z, cmap=my_cmap)
# ax3.bar3d(x, y, bottom, width, depth, a_top, shade=True)
ax3.set_title(f'Różnica w czasie wykonania programów\nt(async)-t(sync)\n przy {totalThreads} wątkach')
ax3.set_xlabel('log10 z pracy na buforze')
ax3.set_ylabel('log10 z pracy agentów')
ax3.set_zlabel('Czas [s]')
ax3.view_init(30, -135)
ax3.set_zlim(z_min, z_max)



##############################################################
plt.show()
fig.savefig('Figure_1.png')
