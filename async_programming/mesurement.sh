export CLASSPATH="$PWD/out/production/async_programming"

for producers in 5 10 20
do
  for consumers in 5 10 20
  do
    for work in 1 10 100 1000 10000
    do
      echo $producers $consumers $work `java Main --producers=$producers --consumers=$consumers --work=$work`
    done
  done
done

