<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class riwayatBahanPokok extends Model
{
    public function bahanPokoks(){
        return $this->hasMany('App\bahanPokok','bahan_pokok_id','bahan_pokok_id');
    }
}
