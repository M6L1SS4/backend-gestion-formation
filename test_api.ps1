# Script de test pour l'API de gestion de formation

# 1. Test d'authentification
Write-Host "=== Test d'authentification ==="
$authBody = @{
    email = "admin@formation.com"
    motDePasse = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body $authBody
$token = $response.token

Write-Host "Token obtenu: $($token.Substring(0, 50))..."
Write-Host "Role: $($response.role)"
Write-Host ""

# 2. Test de création d'un cours
Write-Host "=== Test de création d'un cours ==="
$coursBody = @{
    titre = "Java Spring Boot"
    description = "Formation complète sur Spring Boot"
    duree = 40
    niveau = "Intermédiaire"
} | ConvertTo-Json

$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

try {
    $coursResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/cours" -Method Post -Headers $headers -Body $coursBody
    Write-Host "Cours créé avec succès - ID: $($coursResponse.id)"
    Write-Host "Titre: $($coursResponse.titre)"
} catch {
    Write-Host "Erreur lors de la création du cours: $($_.Exception.Message)"
}

Write-Host ""

# 3. Test de récupération des cours
Write-Host "=== Test de récupération des cours ==="
try {
    $coursList = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/cours" -Method Get -Headers $headers
    Write-Host "Nombre de cours: $($coursList.Count)"
    foreach ($cours in $coursList) {
        Write-Host "- $($cours.titre) (ID: $($cours.id))"
    }
} catch {
    Write-Host "Erreur lors de la récupération des cours: $($_.Exception.Message)"
}

Write-Host ""
Write-Host "=== Tests terminés ==="
