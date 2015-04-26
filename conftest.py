from pyspark import sql
import pyspark
import pytest

@pytest.fixture(scope="session")
def sc():
    return pyspark.SparkContext()

@pytest.fixture(scope="session")
def sqlCtx(sc):
    return sql.HiveContext(sc)
