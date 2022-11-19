import matplotlib.pyplot as plt
import numpy as np

# async
results_async_list = []
with open("results_async.txt", "r") as file:
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
with open("results_sync.txt", "r") as file:
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


workDone = 1000

fig = plt.figure(figsize=(10, 4))
ax1 = fig.add_subplot(121, projection='3d')
ax2 = fig.add_subplot(122, projection='3d')


width = depth = 1
x = [ prod for prod, cons, work, _, _, _ in results_async_list if work == workDone]
y = [ cons for prod, cons, work, _, _, _ in results_async_list if work == workDone]
async_times = [ time[0] for prod, cons, work, time, _, _ in results_async_list if work == workDone]
bottom = np.zeros_like(async_times)

ax1.bar3d(x, y, bottom, width, depth, async_times, shade=True)
ax1.set_title(f'Czas wykonania programu asynchronicznego\ndla pracy={workDone}')
ax1.set_xlabel('Producenci')
ax1.set_ylabel('Konsumenci')
ax1.set_zlabel('Czas [s]')


x = [ prod for prod, cons, work, _, _, _ in results_sync_list if work == workDone]
y = [ cons for prod, cons, work, _, _, _ in results_sync_list if work == workDone]
sync_times = [ time[0] for prod, cons, work, time, _, _ in results_sync_list if work == workDone]
bottom = np.zeros_like(sync_times)

# print([ prod for prod, cons, work, _, _, _ in results_async_list] == [ prod for prod, cons, work, _, _, _ in results_sync_list])

# print(results_sync_list)
ax2.bar3d(x, y, bottom, width, depth, sync_times, shade=True)
ax2.set_title(f'Czas wykonania programu synchronicznego\ndla pracy={workDone}.')
ax2.set_xlabel('Producenci')
ax2.set_ylabel('Konsumenci')
ax2.set_zlabel('Czas [s]')



plt.show()
fig.savefig('Figure_3.png')