<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class riwayatBahanPokok extends Model
{
    protected $primaryKey = 'riwayat_bahan_pokok_id';
    public function bahanPokok(){
        return $this->belongsTo('App\bahanPokok','bahan_pokok_id','bahan_pokok_id');
    }

    public function supplier(){
        return $this->hasOne('App\supplier','supplier_id','supplier_id');
    }
}
