<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class bahanPokok extends Model
{
    protected $primaryKey = 'bahan_pokok_id';
    public function riwayats(){
        return $this->hasMany('App\riwayatBahanPokok','bahan_pokok_id','bahan_pokok_id');
    }

    public function makananDetails(){
        return $this->hasMany('App\makananDetail','bahan_pokok_id','bahan_pokok_id');
    }
}
