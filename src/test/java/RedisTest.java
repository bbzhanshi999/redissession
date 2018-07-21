import com.neusoft.redissession.redis.RedisConnection;
import com.neusoft.redissession.redis.RedisManager;
import org.junit.jupiter.api.Test;

public class RedisTest {



    @Test
    public void test1(){
        RedisManager re = RedisManager.getInstance();
        RedisConnection redisConnection = re.getConnection();
        redisConnection.hset("session:"+2,"creationTime",System.currentTimeMillis());

        //设置过期时间
        redisConnection.expire("session:"+2,1800);
    }

    @Test
    public void test2(){
        RedisManager re = RedisManager.getInstance();
        RedisConnection redisConnection = re.getConnection();
        //redisConnection.hset("session:"+1,"creationTime",System.currentTimeMillis());
        System.out.println(redisConnection.hget("session:"+1,"creationTime"));

    }

    @Test
    public void test3(){
        RedisManager re = RedisManager.getInstance();
        RedisConnection redisConnection = re.getConnection();
        redisConnection.expire("session:"+1,50000);
    }

    @Test
    public void test4(){
        RedisManager re = RedisManager.getInstance();
        RedisConnection redisConnection = re.getConnection();
        System.out.println(redisConnection.exists("session:"+1));
        System.out.println(redisConnection.isConnected());
        System.out.println(redisConnection.hdel("session:"+1,"creationTime"));
    }

    @Test
    public void test5(){
        RedisManager re = RedisManager.getInstance();
        RedisConnection redisConnection = re.getConnection();
        redisConnection.del("session:"+1);
    }

    @Test
    public void test6() throws InterruptedException {
        RedisManager re = RedisManager.getInstance();
        RedisConnection redisConnection = re.getConnection();
        redisConnection.hset("session:"+2,"creationTime",System.currentTimeMillis());
        redisConnection.close();
        Thread.sleep(10000);
        System.out.println(redisConnection.isConnected());
        System.out.println(redisConnection.hget("session:"+2,"creationTime"));
    }

}
