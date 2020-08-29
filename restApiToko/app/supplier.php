<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class supplier extends Model
{
    protected $primaryKey = 'supplier_id';
    //
    public function riwayatBahanPokok(){
        return $this->belongsTo('App\riwayatBahanPokok','supplier_id','supplier_id');
    }
}
