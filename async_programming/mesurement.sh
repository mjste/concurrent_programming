export CLASSPATH="$PWD/out/production/async_programming"

for producers in 1 2 4 8 16 32; do
  for consumers in 1 2 4 8 16 32; do
    if ((consumers <= producers)); then
      for work in 10 100 1000; do
        for tries in {1..10}; do

          echo $producers $consumers $work $(java Main --producers=$producers --consumers=$consumers --work=$work --type=sync )
        done
      done
    fi
  done
done


