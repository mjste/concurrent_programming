export CLASSPATH="$PWD/out/production/async_programming"

#rm sync.txt
#rm async.txt
#
#for half_threads in 1 2 4 ; do
#  for object_work in 100 1000 10000 100000 ; do
#    for agent_work in 100 1000 10000 100000 ; do
#      for tries in {1..10}; do
#        echo $((half_threads * 2)) $object_work $agent_work $(java Main --producers=$half_threads --consumers=$half_threads --objectWork=$object_work --agentWork=$agent_work --type=async) | tee -a async.txt
#        echo $((half_threads * 2)) $object_work $agent_work $(java Main --producers=$half_threads --consumers=$half_threads --objectWork=$object_work --agentWork=$agent_work --type=sync) | tee -a sync.txt
#      done
#    done
#  done
#done

for half_threads in 4 ; do
  for object_work in 100000 ; do
    for agent_work in 100 1000 10000 100000 ; do
      for tries in {1..10}; do
        echo $((half_threads * 2)) $object_work $agent_work $(java Main --producers=$half_threads --consumers=$half_threads --objectWork=$object_work --agentWork=$agent_work --type=async) | tee -a async.txt
        echo $((half_threads * 2)) $object_work $agent_work $(java Main --producers=$half_threads --consumers=$half_threads --objectWork=$object_work --agentWork=$agent_work --type=sync) | tee -a sync.txt
      done
    done
  done
done