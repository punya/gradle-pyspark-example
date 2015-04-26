#!/usr/bin/env python

from setuptools import setup
import versioneer

versioneer.VCS = 'git'
versioneer.versionfile_source = 'artichoke/_version.py'
versioneer.versionfile_build = None
versioneer.tag_prefix = ''
versioneer.parentdir_prefix = ''

setup(
    name='Artichoke',
    version=versioneer.get_version(),
    cmdclass=versioneer.get_cmdclass(),
    packages=['artichoke']
)
