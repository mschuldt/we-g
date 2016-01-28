gestures = ["circle", "up-down", "flip-roll"]
print "gesture, max, min:"
for g in gestures:
    max_v = -9999999999
    min_v = -max_v
    for i in range(20):
        f = open("raw_gesture_recordings/{}/{}.csv".format(g, i), "r")
        lines = f.readlines()[1:]
        f.close()
        f = open("raw_gesture_recordings_converted/{}/{}.csv".format(g, i), "w")
                
        for line in lines:
            time,x,y,z = map(lambda x: float(x.strip()), line.split(","))


            def c(n):
                if n > 0x8000:
                    v = 0x10000 - n
                else:
                    v = -n
                return v / float(2**15)


            xx,yy,zz = map(c, (x,y,z))
            for n in (xx,yy,zz):
                if n > max_v:
                    max_v = n
                if n < min_v:
                    min_v = n            
            new = "{},{},{},{}\n".format(time, xx, yy, zz)
            f.write(new)
        f.close()
    print g, max_v, min_v
