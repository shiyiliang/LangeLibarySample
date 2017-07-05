package shiyiliang.me.baselibary.util;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-07-05
 * Desc  :
 */

public class AppUtil {
    public static void checkNull(Object obj) {
        if (obj == null)
            throw new RuntimeException(obj.getClass().getSimpleName() + " is null");
    }
}
