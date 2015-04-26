def test_something(sqlCtx):
    assert sqlCtx.sql("""
        select explode(array(1, 2, 3))
    """).agg({"_c0": "sum"}).collect()[0][0] == 6
