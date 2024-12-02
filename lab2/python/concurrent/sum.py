import sys
from collections import defaultdict
import threading

multiplex = threading.Semaphore(1)
file_sums = defaultdict(list)

counter = 0

def do_sum(path, flow_controller):

    flow_controller.acquire()
    
    global counter
    
    _sum = 0
    
    with open(path, 'rb') as f:
        byte = f.read(1)
        while byte:
            _sum += int.from_bytes(byte, byteorder='big', signed=False)
            byte = f.read(1)
    multiplex.acquire()

    file_sums[_sum].append(path)
    counter += _sum
    
    multiplex.release()
    
    flow_controller.release()

#many error could be raised error. we don't care       
if __name__ == "__main__":
    paths = sys.argv[1:]
    threads = []
    
    n = 1
    if(type(paths) == 'list'):
        n = len(paths)//2
    
    flow_controller = threading.Semaphore(n)

    for path in paths:
        try:
            thread = threading.Thread(target=do_sum, args=(path,
                flow_controller,))
            thread.start()
            threads.append(thread)
        except Exception as e:
            print(f"Erro ao processar {path}: {e}")
    for thread in threads:
        thread.join()
    print("dict" + str(file_sums))
    print("Total: " + str(counter))

    for sum_value, files in file_sums.items():
        if len(files) > 1:
            print(f"{sum_value} {' '.join(files)}")

