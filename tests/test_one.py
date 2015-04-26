from artichoke import factorial
from pyspark import sql
import pyspark

def test_something(sqlCtx):
    assert sqlCtx.sql("""
        select explode(array(1, 2, 3))
    """).agg({"_c0": "sum"}).collect()[0][0] == 6

def test_java(sc):
    assert sc._jvm.fact.Factorial.fact(6) == factorial.fact(6)

def test_remote(sc):
    assert sc.parallelize([1, 2, 3]).map(factorial.fact).collect() == [1, 2, 6]

def test_remote_java(sqlCtx):
    sqlCtx.createDataFrame([sql.Row(x = 1), sql.Row(x = 2), sql.Row(x = 3)])
