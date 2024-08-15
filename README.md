# gal-code-challenge

- [gal-code-challenge](#gal-code-challenge)
  - [Sonar Report](#sonar-report)
  - [Docker image build](#docker-image-build)
    - [Trivy vulnerabilities report](#trivy-vulnerabilities-report)


## Sonar Report
![image](https://github.com/user-attachments/assets/8ddbd5b3-fccf-4997-b2ca-dbe30e6aa21b)
ad

## Docker image build

```bash
docker build -t gal-code-challenge .
```

### Trivy vulnerabilities report

```
2024-08-15T02:16:37Z    INFO    Detected OS     family="alpine" version="3.20.2"
2024-08-15T02:16:37Z    INFO    [alpine] Detecting vulnerabilities...   os_version="3.20" repository="3.20" pkg_num=42
2024-08-15T02:16:37Z    INFO    Number of language-specific files       num=1
2024-08-15T02:16:37Z    INFO    [jar] Detecting vulnerabilities...

gal-code-challenge:latest (alpine 3.20.2)
=========================================
Total: 0 (UNKNOWN: 0, LOW: 0, MEDIUM: 0, HIGH: 0, CRITICAL: 0)
```