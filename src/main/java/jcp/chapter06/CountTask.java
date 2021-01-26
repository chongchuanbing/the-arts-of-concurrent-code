package jcp.chapter06;import java.util.concurrent.ExecutionException;import java.util.concurrent.ForkJoinPool;import java.util.concurrent.Future;import java.util.concurrent.RecursiveTask;/** * ���������� *  * @author chongchuanbing * @version $Id: CountTask.java, v 0.1 2015-8-1 ����12:00:29 chongchuanbing Exp $ */public class CountTask extends RecursiveTask<Integer> {    private static final int THRESHOLD = 2; // ��ֵ    private int              start;    private int              end;    public CountTask(int start, int end) {        this.start = start;        this.end = end;    }    @Override    protected Integer compute() {        int sum = 0;        // ��������㹻С�ͼ�������        boolean canCompute = (end - start) <= THRESHOLD;        if (canCompute) {            for (int i = start; i <= end; i++) {                sum += i;            }        } else {            // ������������ֵ���ͷ��ѳ��������������            int middle = (start + end) / 2;            CountTask leftTask = new CountTask(start, middle);            CountTask rightTask = new CountTask(middle + 1, end);            //ִ��������            leftTask.fork();            rightTask.fork();            //�ȴ�������ִ���꣬���õ�����            int leftResult = leftTask.join();            int rightResult = rightTask.join();            //�ϲ�������            sum = leftResult + rightResult;        }        return sum;    }    public static void main(String[] args) {        ForkJoinPool forkJoinPool = new ForkJoinPool();        // ����һ���������񣬸������1+2+3+4        CountTask task = new CountTask(1, 4);        // ִ��һ������        Future<Integer> result = forkJoinPool.submit(task);        try {            System.out.println(result.get());        } catch (InterruptedException e) {        } catch (ExecutionException e) {        }    }}